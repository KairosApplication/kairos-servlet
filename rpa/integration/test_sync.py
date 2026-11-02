"""Real DB acceptance tests. Run only in the isolated Compose demo, after UI setup."""
import copy
import os
import unittest
import uuid
from concurrent.futures import ThreadPoolExecutor
from unittest.mock import patch

from sync import (IntegrationError, connect_checked, extract, import_purchase, load_manifest,
                  prepare_source, read_mapping, synchronize, verify_target)


@unittest.skipUnless(os.environ.get('RPA_INTEGRATION_TESTS') == 'true', 'Requires isolated real demo databases')
class RealDatabaseTests(unittest.TestCase):
    def setUp(self):
        self.source = connect_checked('SOURCE')
        self.target = connect_checked('TARGET')
        self.config = read_mapping('integration/mapping.json')
        self.source_id = prepare_source(self.source)
        entries = load_manifest('integration/cases.csv', os.environ['RPA_TEST_STATE'],
                                os.environ.get('RPA_TEST_BASE', 'http://127.0.0.1:8098'))
        self.cases = {p['ref']: p for p in extract(self.source, entries)}
        self.run_id = uuid.uuid4()

    def tearDown(self):
        self.source.close()
        self.target.close()

    def counts(self):
        return self.target.execute('''SELECT (SELECT count(*) FROM Purchases) AS purchases,
            (SELECT count(*) FROM Purchase_Products) AS items,
            (SELECT count(*) FROM rpa_integration.purchases) AS imported,
            (SELECT count(*) FROM Audits) AS audits''').fetchone()

    def transfer(self, name, config=None, fault=None):
        return import_purchase(self.target, self.source_id, self.cases[name], config or self.config,
                               self.run_id, fault=fault)

    def test_a_missing_mapping_audited_and_recoverable(self):
        config = copy.deepcopy(self.config)
        del config['products']['2']
        before = self.counts()
        entry = next(e for e in load_manifest('integration/cases.csv', os.environ['RPA_TEST_STATE'],
                    os.environ['RPA_TEST_BASE']) if e[1]['ref'] == 'FAIL-MAP')
        result = synchronize(self.source, self.target, [entry], config, self.run_id)
        self.assertEqual(result['results'][0]['reason'], 'missing_product_mapping:2')
        self.assertEqual(self.counts(), before)
        audit = self.target.execute('''SELECT status,reason FROM rpa_integration.attempts
            WHERE run_id=%s''', (self.run_id,)).fetchone()
        self.assertEqual(audit['status'], 'rejected')
        self.assertIn(self.transfer('FAIL-MAP')['result'], ('imported', 'already_imported'))

    def test_b_item_sql_failure_rolls_back_header_items_and_audit_trigger(self):
        # Repeated tests use already-imported rows: drift is tested separately.
        existing = self.target.execute('''SELECT 1 FROM rpa_integration.purchases
            WHERE source_instance=%s AND legacy_purchase_id=%s''',
            (self.source_id, self.cases['FAIL-ITEM']['id'])).fetchone()
        if existing:
            self.assertEqual(self.transfer('FAIL-ITEM')['result'], 'already_imported')
            return
        before = self.counts()
        def fail(stage, connection):
            if stage == 'after_item':
                connection.execute('SELECT 1/0')  # Genuine database error after an item INSERT.
        with self.assertRaises(Exception):
            self.transfer('FAIL-ITEM', fault=fail)
        self.assertEqual(self.counts(), before)
        self.assertEqual(self.transfer('FAIL-ITEM')['result'], 'imported')

    def test_c_connection_interruption_and_recovery(self):
        existing = self.target.execute('''SELECT 1 FROM rpa_integration.purchases
            WHERE source_instance=%s AND legacy_purchase_id=%s''',
            (self.source_id, self.cases['INTERRUPT']['id'])).fetchone()
        if existing:
            self.assertEqual(self.transfer('INTERRUPT')['result'], 'already_imported')
            return
        before = self.counts()
        def interrupt(stage, connection):
            if stage == 'after_header':
                connection.close()  # Real client loss with an open transaction.
                raise RuntimeError('simulated_connection_loss')
        with self.assertRaises(Exception):
            self.transfer('INTERRUPT', fault=interrupt)
        self.target = connect_checked('TARGET')
        self.assertEqual(self.counts(), before)
        self.assertEqual(self.transfer('INTERRUPT')['result'], 'imported')

    def test_d_cross_company_and_invalid_customer_rejected(self):
        before = self.counts()
        config = copy.deepcopy(self.config)
        config['products']['1'].update(target_id=20001, target_name='Produto outra empresa')
        with self.assertRaises(IntegrationError):
            self.transfer('COMPANY', config)
        config = copy.deepcopy(self.config)
        config['customer_id'] = 502
        with self.assertRaises(IntegrationError):
            self.transfer('CUSTOMER', config)
        self.assertEqual(self.counts(), before)
        self.transfer('COMPANY')
        self.transfer('CUSTOMER')

    def test_e_concurrent_replay_has_one_persistent_link(self):
        existed = self.target.execute('''SELECT 1 FROM rpa_integration.purchases
            WHERE source_instance=%s AND legacy_purchase_id=%s''',
            (self.source_id, self.cases['CONCURRENT']['id'])).fetchone()
        def task():
            with connect_checked('TARGET') as connection:
                return import_purchase(connection, self.source_id, self.cases['CONCURRENT'], self.config,
                                       uuid.uuid4())['result']
        with ThreadPoolExecutor(max_workers=2) as pool:
            results = list(pool.map(lambda _: task(), range(2)))
        self.assertEqual(sorted(results), ['already_imported', 'already_imported'] if existed
                         else ['already_imported', 'imported'])
        rows = self.target.execute('''SELECT count(*) AS n FROM rpa_integration.purchases
            WHERE source_instance=%s AND legacy_purchase_id=%s''',
            (self.source_id, self.cases['CONCURRENT']['id'])).fetchone()
        self.assertEqual(rows['n'], 1)

    def test_f_integrity_reconciliation_and_no_completed_sales(self):
        rows = self.target.execute('SELECT * FROM rpa_integration.purchases').fetchall()
        for row in rows:
            result = verify_target(self.target, row['purchase_id'], row['target_snapshot'])
            self.assertEqual(len(result['items']), row['item_count'])
            self.assertNotEqual(row['purchase_id'], row['legacy_purchase_id'])
        self.assertEqual(self.target.execute("SELECT count(*) AS n FROM Purchases WHERE status='COMPLETED'").fetchone()['n'], 0)
        self.assertEqual(self.target.execute('SELECT count(*) AS n FROM vw_bi_sales_14d').fetchone()['n'], 0)
        self.assertEqual(self.target.execute('SELECT count(*) AS n FROM Product_Inventories').fetchone()['n'], 0)

    def test_g_main_database_and_remote_hosts_refused_before_connection(self):
        for setting in ({'TARGET_DATABASE': 'kairosdb'}, {'TARGET_DATABASE': 'kairos_demo'},
                        {'TARGET_HOST': 'production.example.invalid'}, {'RPA_AUTHORIZE_TARGET': ''}):
            with self.subTest(setting=setting), patch.dict(os.environ, setting):
                with self.assertRaises(IntegrationError):
                    connect_checked('TARGET')

    def test_h_changed_source_not_silently_reimported(self):
        self.transfer('FAIL-MAP')
        altered = copy.deepcopy(self.cases['FAIL-MAP'])
        altered['items'][0]['quantidade_item'] += 1
        before = self.counts()
        with self.assertRaises(IntegrationError):
            import_purchase(self.target, self.source_id, altered, self.config, self.run_id)
        self.assertEqual(self.counts(), before)

    def test_i_database_unavailability_fails_without_business_writes(self):
        import psycopg
        before = self.counts()
        for role in ('SOURCE', 'TARGET'):
            with self.subTest(role=role), patch.dict(os.environ, {role + '_PORT': '1'}):
                with self.assertRaises(psycopg.OperationalError):
                    connect_checked(role)
        self.assertEqual(self.counts(), before)


if __name__ == '__main__':
    unittest.main()
