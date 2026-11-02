"""Atomic, idempotent transfer of RPA-confirmed legacy purchases to a local demo."""
import argparse
import hashlib
import json
import os
import sys
import uuid
from datetime import datetime
from decimal import Decimal
from pathlib import Path

import psycopg
from psycopg.rows import dict_row
from psycopg.types.json import Jsonb

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from robot import fingerprint, load_plan, save_json, validate_url

SOURCE_DB = 'kairos_servlet_demo'
TARGET_DB = 'kairos_normalized_demo'
PURPOSE = 'kairos-interdisciplinary-local-demo'


class IntegrationError(Exception):
    pass


def digest(value):
    return hashlib.sha256(json.dumps(value, sort_keys=True, separators=(',', ':')).encode()).hexdigest()


def positive(value):
    return type(value) is int and 0 < value <= 2147483647


def read_mapping(path):
    config = json.loads(Path(path).read_text(encoding='utf-8'))
    if (set(config) != {'company_id', 'customer_id', 'products', 'status_policy', 'price_policy'}
            or not positive(config['company_id']) or not positive(config['customer_id'])
            or config['status_policy'] != 'IN_PROGRESS'
            or config['price_policy'] != 'current_demo_price'
            or not isinstance(config['products'], dict) or not config['products']):
        raise IntegrationError('invalid_import_policy')
    targets = []
    for key, product in config['products'].items():
        if (not key.isdigit() or not positive(int(key)) or str(int(key)) != key
                or set(product) != {'target_id', 'source_name', 'target_name'}
                or not positive(product['target_id'])
                or not all(isinstance(product[k], str) and product[k] for k in ('source_name', 'target_name'))):
            raise IntegrationError('invalid_product_mapping')
        targets.append(product['target_id'])
    if len(set(targets)) != len(targets):
        raise IntegrationError('mapping_must_be_one_to_one')
    return config


def connect_checked(role):
    expected = SOURCE_DB if role == 'SOURCE' else TARGET_DB
    name = os.environ.get(role + '_DATABASE', expected)
    host = os.environ.get(role + '_HOST', 'db' if role == 'SOURCE' else 'normalized')
    if name != expected or host not in {'db', 'normalized', 'localhost', '127.0.0.1'}:
        raise IntegrationError('unauthorized_' + role.lower() + '_destination')
    if os.environ.get('RPA_AUTHORIZE_TARGET') != TARGET_DB:
        raise IntegrationError('target_requires_explicit_authorization')
    connection = psycopg.connect(host=host, port=int(os.environ.get(role + '_PORT', '5432')),
        dbname=name, user=os.environ.get(role + '_USER', 'demo'),
        password=os.environ.get(role + '_PASSWORD'), connect_timeout=5,
        autocommit=True, row_factory=dict_row,
        options='-c timezone=America/Sao_Paulo -c statement_timeout=15000 -c lock_timeout=5000')
    try:
        if connection.execute('SELECT current_database() AS name').fetchone()['name'] != expected:
            raise IntegrationError('actual_database_mismatch')
        if role == 'TARGET':
            marker = connection.execute('SELECT * FROM rpa_integration.environment WHERE singleton').fetchone()
            if not marker or marker['purpose'] != PURPOSE:
                raise IntegrationError('target_has_no_demo_marker')
        return connection
    except BaseException:
        connection.close()
        raise


def prepare_source(connection):
    # Only integration metadata is added. Business tables remain unchanged.
    with connection.transaction():
        connection.execute('CREATE SCHEMA IF NOT EXISTS rpa_source')
        connection.execute('''CREATE TABLE IF NOT EXISTS rpa_source.identity (
            singleton boolean PRIMARY KEY CHECK(singleton),
            instance_id uuid NOT NULL DEFAULT gen_random_uuid())''')
        connection.execute('INSERT INTO rpa_source.identity(singleton) VALUES(true) ON CONFLICT DO NOTHING')
    return connection.execute('SELECT instance_id FROM rpa_source.identity WHERE singleton').fetchone()['instance_id']


def load_manifest(csv_path, state_path, base_url):
    plan = load_plan(csv_path)
    state = json.loads(Path(state_path).read_text(encoding='utf-8'))
    if state['database'] != SOURCE_DB or state['target'] != validate_url(base_url):
        raise IntegrationError('rpa_journal_target_mismatch')
    entries = []
    seen = set()
    for group in plan:
        entry = state['purchases'].get(group['ref'])
        if not entry or entry['state'] != 'complete' or entry['fingerprint'] != fingerprint(group):
            raise IntegrationError('purchase_not_confirmed_by_rpa:' + group['ref'])
        if not positive(entry['purchase_id']) or entry['purchase_id'] in seen:
            raise IntegrationError('invalid_legacy_purchase_id')
        seen.add(entry['purchase_id'])
        entries.append((entry['purchase_id'], group))
    return entries


def extract(connection, entries):
    extracted = []
    # One consistent snapshot, explicitly READ ONLY. No cursor/watermark can skip a retry.
    with connection.transaction():
        connection.execute('SET TRANSACTION ISOLATION LEVEL REPEATABLE READ, READ ONLY')
        for purchase_id, group in entries:
            purchase = connection.execute('SELECT id,data_compra FROM compras WHERE id=%s', (purchase_id,)).fetchone()
            if not purchase or purchase['data_compra'] != datetime.fromisoformat(group['date']):
                raise IntegrationError('source_purchase_differs_from_rpa:' + str(purchase_id))
            items = connection.execute('''SELECT cp.id, cp.produto_id, cp.quantidade_item, p.nome
                FROM compras_produtos cp JOIN produtos p ON p.id=cp.produto_id
                WHERE cp.compra_id=%s ORDER BY cp.id''', (purchase_id,)).fetchall()
            quantities = {i['produto_id']: i['quantidade_item'] for i in items}
            if len(items) != len(quantities) or quantities != group['items']:
                raise IntegrationError('source_items_differ_from_rpa:' + str(purchase_id))
            extracted.append({'id': purchase_id, 'ref': group['ref'],
                'date': purchase['data_compra'].isoformat(), 'items': items})
    return extracted


def policy_for(config, purchase):
    products = {}
    for item in purchase['items']:
        key = str(item['produto_id'])
        if key not in config['products']:
            raise IntegrationError('missing_product_mapping:' + key)
        if item['nome'] != config['products'][key]['source_name']:
            raise IntegrationError('source_product_name_mismatch:' + key)
        if not positive(item['quantidade_item']):
            raise IntegrationError('invalid_source_quantity')
        products[key] = config['products'][key]
    return {**config, 'products': products}


def audit(target, run_id, source_id, purchase, status, target_id=None, reason=None):
    target.execute('''INSERT INTO rpa_integration.attempts
        (run_id,source_instance,legacy_purchase_id,purchase_id,status,item_count,reason)
        VALUES(%s,%s,%s,%s,%s,%s,%s)''',
        (run_id, source_id, purchase['id'], target_id, status, len(purchase['items']), reason))


def verify_target(target, target_id, expected):
    header = target.execute('''SELECT p.customer_id,p.date,p.status,c.company_id
        FROM Purchases p JOIN Customers c ON c.id=p.customer_id WHERE p.id=%s''', (target_id,)).fetchone()
    if (not header or header['customer_id'] != expected['customer_id']
            or header['company_id'] != expected['company_id'] or header['status'] != 'IN_PROGRESS'
            or header['date'] != datetime.fromisoformat(expected['date'])):
        raise IntegrationError('target_header_drift')
    rows = target.execute('''SELECT pp.product_id,pp.quantity,pp.amount::numeric AS unit_price,p.company_id
        FROM Purchase_Products pp JOIN Products p ON p.id=pp.product_id
        WHERE pp.purchase_id=%s ORDER BY pp.product_id''', (target_id,)).fetchall()
    actual = [{'product_id': r['product_id'], 'quantity': r['quantity'], 'unit_price': str(r['unit_price'])}
              for r in rows]
    if actual != expected['items'] or any(r['company_id'] != expected['company_id'] for r in rows):
        raise IntegrationError('target_item_or_company_drift')
    return {'purchase_id': target_id, 'customer_id': header['customer_id'],
            'company_id': header['company_id'], 'status': header['status'], 'items': actual}


def import_purchase(target, source_id, purchase, config, run_id, fault=None):
    # SQL advisory lock + UNIQUE(source UUID, legacy ID), not timestamps or names.
    with target.transaction():
        target.execute('SELECT pg_advisory_xact_lock(hashtextextended(%s,0))', (f'{source_id}:{purchase["id"]}',))
        policy = policy_for(config, purchase)
        source_hash = digest({k: v for k, v in purchase.items() if k != 'ref'})
        policy_hash = digest(policy)
        previous = target.execute('''SELECT * FROM rpa_integration.purchases
            WHERE source_instance=%s AND legacy_purchase_id=%s FOR UPDATE''', (source_id, purchase['id'])).fetchone()
        if previous:
            if previous['source_hash'] != source_hash or previous['policy_hash'] != policy_hash:
                raise IntegrationError('previous_import_source_or_mapping_changed')
            verified = verify_target(target, previous['purchase_id'], previous['target_snapshot'])
            audit(target, run_id, source_id, purchase, 'already_imported', previous['purchase_id'])
            return {'legacy_id': purchase['id'], 'ref': purchase['ref'], 'result': 'already_imported',
                    'source_item_count': len(purchase['items']), 'verified': verified}

        customer = target.execute('SELECT company_id FROM Customers WHERE id=%s FOR SHARE', (config['customer_id'],)).fetchone()
        if not customer or customer['company_id'] != config['company_id']:
            raise IntegrationError('invalid_customer_or_company')
        items = []
        for item in purchase['items']:
            product_id = policy['products'][str(item['produto_id'])]['target_id']
            product = target.execute('SELECT company_id,name,price::numeric AS price FROM Products WHERE id=%s FOR SHARE',
                                     (product_id,)).fetchone()
            if not product or product['company_id'] != config['company_id']:
                raise IntegrationError('invalid_target_product_or_company:' + str(product_id))
            if product['name'] != policy['products'][str(item['produto_id'])]['target_name']:
                raise IntegrationError('target_product_name_mismatch:' + str(product_id))
            if product['price'] is None or product['price'] < 0:
                raise IntegrationError('missing_or_invalid_demo_price')
            items.append({'product_id': product_id, 'quantity': item['quantidade_item'],
                          'unit_price': str(product['price'])})
        items.sort(key=lambda item: item['product_id'])
        snapshot = {'date': purchase['date'], 'company_id': config['company_id'],
                    'customer_id': config['customer_id'], 'status': 'IN_PROGRESS', 'items': items}
        new_id = target.execute('''INSERT INTO Purchases(customer_id,date,status)
            VALUES(%s,%s,'IN_PROGRESS') RETURNING id''', (config['customer_id'], datetime.fromisoformat(purchase['date']))).fetchone()['id']
        if fault:
            fault('after_header', target)
        for item in items:
            target.execute('''INSERT INTO Purchase_Products(purchase_id,product_id,quantity,amount)
                VALUES(%s,%s,%s,%s::numeric::money)''',
                (new_id, item['product_id'], item['quantity'], Decimal(item['unit_price'])))
            if fault:
                fault('after_item', target)
        verified = verify_target(target, new_id, snapshot)
        target.execute('''INSERT INTO rpa_integration.purchases
            (source_instance,legacy_purchase_id,purchase_id,source_hash,policy_hash,source_snapshot,
             target_snapshot,item_count,price_origin) VALUES(%s,%s,%s,%s,%s,%s,%s,%s,'current_demo_price')''',
            (source_id, purchase['id'], new_id, source_hash, policy_hash, Jsonb(purchase), Jsonb(snapshot), len(items)))
        audit(target, run_id, source_id, purchase, 'imported', new_id)
        return {'legacy_id': purchase['id'], 'ref': purchase['ref'], 'result': 'imported',
                'source_item_count': len(purchase['items']), 'verified': verified}


def safe_error(error):
    if isinstance(error, IntegrationError):
        return str(error)
    if isinstance(error, psycopg.Error):
        return 'database_error:' + (error.sqlstate or type(error).__name__)
    return 'input_or_runtime_error:' + type(error).__name__


def reconcile(target, source_id, purchases, accepted):
    rows = []
    with target.transaction():
        target.execute('SET TRANSACTION READ ONLY')
        for purchase in purchases:
            if purchase['id'] not in accepted:
                continue
            link = target.execute('''SELECT * FROM rpa_integration.purchases
                WHERE source_instance=%s AND legacy_purchase_id=%s''', (source_id, purchase['id'])).fetchone()
            if not link or link['source_hash'] != digest({k: v for k, v in purchase.items() if k != 'ref'}):
                raise IntegrationError('reconciliation_source_or_link_mismatch')
            actual = verify_target(target, link['purchase_id'], link['target_snapshot'])
            source_units = sum(i['quantidade_item'] for i in purchase['items'])
            target_units = sum(i['quantity'] for i in actual['items'])
            if source_units != target_units:
                raise IntegrationError('reconciliation_quantity_mismatch')
            rows.append({'legacy_id': purchase['id'], 'normalized_id': link['purchase_id'],
                         'source_items': len(purchase['items']), 'target_items': len(actual['items']),
                         'source_units': source_units, 'target_units': target_units, 'matches': True})
    return {'status': 'matched', 'purchases': len(rows), 'rows': rows}


def synchronize(source, target, entries, config, run_id):
    source_id = prepare_source(source)
    purchases = extract(source, entries)
    results = []
    for purchase in purchases:
        try:
            results.append(import_purchase(target, source_id, purchase, config, run_id))
        except Exception as error:
            reason = safe_error(error)
            # import_purchase's transaction has rolled back before this audit write.
            audit(target, run_id, source_id, purchase, 'rejected', reason=reason)
            results.append({'legacy_id': purchase['id'], 'ref': purchase['ref'], 'result': 'rejected', 'reason': reason})
    accepted = {r['legacy_id'] for r in results if r['result'] != 'rejected'}
    reconciliation = reconcile(target, source_id, purchases, accepted)
    return {'source_instance': str(source_id), 'results': results, 'reconciliation': reconciliation,
            'status': 'failed' if any(r['result'] == 'rejected' for r in results) else 'success'}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--csv', default='compras.csv')
    parser.add_argument('--state', default='output/state.json')
    parser.add_argument('--base-url', default='http://127.0.0.1:8088')
    parser.add_argument('--mapping', default='integration/mapping.json')
    parser.add_argument('--report', required=True)
    parser.add_argument('--run-id', default=str(uuid.uuid4()))
    parser.add_argument('--check-only', action='store_true')
    args = parser.parse_args()
    report = {'run_id': args.run_id, 'started_at': datetime.now().astimezone().isoformat(),
              'target_database': TARGET_DB, 'price_origin': 'current_demo_price', 'imported_status': 'IN_PROGRESS'}
    try:
        config = read_mapping(args.mapping)
        # Verify both destinations before any source metadata or business write.
        with connect_checked('SOURCE') as source, connect_checked('TARGET') as target:
            marker = target.execute('SELECT * FROM rpa_integration.environment').fetchone()
            report['target_instance'] = str(marker['instance_id'])
            report['original_schema_sha256'] = marker['original_schema_sha256']
            if args.check_only:
                report['status'] = 'ready'
            else:
                entries = load_manifest(args.csv, args.state, args.base_url)
                report.update(synchronize(source, target, entries, config, uuid.UUID(args.run_id)))
    except Exception as error:
        report.update(status='failed', reason=safe_error(error))
    report['finished_at'] = datetime.now().astimezone().isoformat()
    path = Path(args.report)
    path.parent.mkdir(parents=True, exist_ok=True)
    save_json(path, report)
    print(json.dumps(report, ensure_ascii=False))
    return 0 if report['status'] in ('ready', 'success') else 1


if __name__ == '__main__':
    sys.exit(main())
