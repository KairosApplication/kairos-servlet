"""Kairos Servlet RPA: browser forms only; never writes to PostgreSQL directly."""
import argparse
import csv
import hashlib
import json
import os
import sys
from datetime import datetime
from pathlib import Path
from urllib.parse import urlsplit
from zoneinfo import ZoneInfo

DEMO_DATABASE = 'kairos_servlet_demo'
FIELDS = ['compra_ref', 'data_compra', 'produto_id', 'quantidade']


def load_plan(path):
    groups = {}
    dates = {}
    with Path(path).open(encoding='utf-8-sig', newline='') as source:
        reader = csv.DictReader(source)
        if reader.fieldnames != FIELDS:
            raise ValueError('Cabecalho CSV deve ser: ' + ','.join(FIELDS))
        for line, row in enumerate(reader, 2):
            if line > 101 or None in row or any(v is None or not v.strip() for v in row.values()):
                raise ValueError(f'Linha {line}: campos vazios/extras ou limite de 100 itens')
            ref = row['compra_ref'].strip()
            if len(ref) > 64 or not all(c.isalnum() or c in '-_' for c in ref):
                raise ValueError(f'Linha {line}: referencia invalida')
            date = datetime.strptime(row['data_compra'].strip(), '%Y-%m-%dT%H:%M')
            if date > datetime.now(ZoneInfo('America/Sao_Paulo')).replace(tzinfo=None):
                raise ValueError(f'Linha {line}: data futura')
            date_key = date.isoformat(timespec='minutes')
            product, quantity = int(row['produto_id']), int(row['quantidade'])
            if not (0 < product <= 2147483647 and 0 < quantity <= 2147483647):
                raise ValueError(f'Linha {line}: IDs e quantidades devem ser inteiros positivos')
            if date_key in dates and dates[date_key] != ref:
                raise ValueError('Referencias diferentes nao podem compartilhar a mesma data/minuto')
            dates[date_key] = ref
            group = groups.setdefault(ref, {'ref': ref, 'date': date_key, 'items': {}})
            if group['date'] != date_key or product in group['items']:
                raise ValueError(f'Linha {line}: referencia com data divergente ou produto duplicado')
            group['items'][product] = quantity
    if not groups:
        raise ValueError('CSV vazio')
    return list(groups.values())


def fingerprint(group):
    canonical = [group['date'], sorted(group['items'].items())]
    return hashlib.sha256(json.dumps(canonical).encode()).hexdigest()


def validate_url(url):
    parts = urlsplit(url)
    if (parts.scheme != 'http' or parts.hostname not in ('localhost', '127.0.0.1')
            or parts.username or parts.password or parts.query or parts.fragment
            or '..' in parts.path):
        raise ValueError('RPA exige URL HTTP local, sem credenciais/query/fragmento')
    return url.rstrip('/')


def save_json(path, value):
    temp = path.with_suffix('.tmp')
    temp.write_text(json.dumps(value, indent=2, ensure_ascii=False), encoding='utf-8')
    temp.replace(path)


class Robot:
    def __init__(self, page, base, journal, save):
        self.page, self.base, self.journal, self.save = page, base, journal, save

    def visit(self, path):
        response = self.page.goto(self.base + path, wait_until='domcontentloaded')
        if not response or response.status >= 400:
            raise RuntimeError('Tela indisponivel: ' + path)
        if response.headers.get('x-kairos-demo-database') != DEMO_DATABASE:
            raise RuntimeError('Instancia nao confirmou o banco exclusivo de demonstracao')

    def login(self, email, password):
        self.visit('/login-page')  # Fail before sending any credentials to non-demo.
        self.page.locator('#email').fill(email)
        self.page.locator('#senha').fill(password)
        self.submit(self.page.locator('form').filter(has=self.page.locator('#email')), '/login')
        if '/admin/compras' not in self.page.url:
            raise RuntimeError('Autenticacao recusada; confira a conta de teste')

    def submit(self, form, path):
        with self.page.expect_response(lambda r: r.request.method == 'POST'
                                       and r.url == self.base + path) as pending:
            form.locator('button[type=submit]').click()
        response = pending.value
        if response.status >= 400:
            raise RuntimeError('Formulario recusado pelo servidor')
        self.page.wait_for_load_state('domcontentloaded')

    def purchases(self):
        self.visit('/admin/compras')
        return self.page.locator('tr[data-compra-id]').evaluate_all(
            '(rows) => rows.map(r => ({id: Number(r.dataset.compraId), date: r.dataset.compraDate}))')

    def items(self, purchase_id):
        rows = self.page.locator(f'#details-row-{purchase_id} tr[data-product-id]').evaluate_all(
            '(rows) => rows.map(r => [Number(r.dataset.productId), Number(r.dataset.quantity)])')
        result = dict(rows)
        if len(rows) != len(result):
            raise RuntimeError('Associacoes duplicadas no banco; revisar manualmente')
        return result

    def preflight(self, plan):
        self.visit('/admin/produtos')
        available = set(self.page.locator('[data-produto-id]').evaluate_all(
            '(rows) => rows.map(r => Number(r.dataset.produtoId))'))
        missing = {p for group in plan for p in group['items']} - available
        if missing:
            raise ValueError('Produtos inexistentes: ' + ','.join(map(str, sorted(missing))))

    def process(self, group):
        ref = group['ref']
        digest = fingerprint(group)
        prior = self.journal.get(ref)
        if prior and prior['fingerprint'] != digest:
            raise ValueError('CSV alterado para referencia ja usada; use nova referencia/data')
        rows = self.purchases()
        matches = [r for r in rows if datetime.fromisoformat(r['date']) == datetime.fromisoformat(group['date'])]
        if len(matches) > 1:
            raise RuntimeError('Data ambigua: mais de uma compra; nenhuma sera alterada')
        if matches:
            purchase_id = matches[0]['id']
            # No external reference column exists: never adopt a manual purchase blindly.
            if not prior:
                raise RuntimeError('Data ja ocupada sem diario correspondente; revisar manualmente')
            if prior.get('purchase_id') not in (None, purchase_id):
                raise RuntimeError('ID diferente do diario; revisar manualmente')
        else:
            if prior:
                raise RuntimeError('Execucao anterior sem compra confirmada; revisar antes de reenviar')
            entry = {'fingerprint': digest, 'state': 'submitting', 'purchase_id': None}
            self.journal[ref] = entry
            self.save()  # Write-ahead: crash/timeout never causes an automatic blind retry.
            self.page.locator('label[for="add-compra-toggle"]').first.click()
            form = self.page.locator('#add-compra-form')
            form.locator('[name=dataCompra]').fill(group['date'])
            self.submit(form, '/admin/compras')
            rows = self.purchases()  # Fresh GET through DAO, not a success message.
            matches = [r for r in rows if datetime.fromisoformat(r['date']) == datetime.fromisoformat(group['date'])]
            if len(matches) != 1:
                raise RuntimeError('Compra nao confirmada de forma univoca; revisar diario')
            purchase_id = matches[0]['id']
        entry = self.journal[ref]
        entry.update(purchase_id=purchase_id, state='linking')
        self.save()
        current = self.items(purchase_id)
        if any(p not in group['items'] or group['items'][p] != q for p, q in current.items()):
            raise RuntimeError('Itens persistidos divergem do CSV; nenhuma correcao automatica')
        added = 0
        for product, quantity in group['items'].items():
            if product in current:
                continue
            details = self.page.locator(f'#details-{purchase_id}')
            if not details.is_checked():
                self.page.locator(f'label[for="details-{purchase_id}"]').click()
            self.page.locator(f'label[for="link-produto-{purchase_id}"]').first.click()
            form = self.page.locator(f'#link-produto-form-{purchase_id}')
            form.locator('[name=idProduto]').fill(str(product))
            form.locator('[name=quantidadeItem]').fill(str(quantity))
            self.submit(form, '/admin/compras-produtos')
            self.visit('/admin/compras')
            current = self.items(purchase_id)
            if current.get(product) != quantity:
                raise RuntimeError('Vinculo nao persistiu com a quantidade esperada')
            added += 1
        if self.items(purchase_id) != group['items']:
            raise RuntimeError('Conferencia final dos itens falhou')
        entry['state'] = 'complete'
        self.save()
        return {'ref': ref, 'purchase_id': purchase_id, 'items': len(current),
                'status': 'confirmed' if added else 'already_confirmed'}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--csv', type=Path, default=Path(__file__).with_name('compras.csv'))
    parser.add_argument('--base-url', default='http://127.0.0.1:8088')
    parser.add_argument('--output', type=Path, default=Path(__file__).with_name('output'))
    parser.add_argument('--headed', action='store_true')
    parser.add_argument('--validate-only', action='store_true')
    args = parser.parse_args()
    report = {'started_at': datetime.now().astimezone().isoformat(), 'results': []}
    lock = None
    try:
        base = validate_url(args.base_url)
        plan = load_plan(args.csv)
        if args.validate_only:
            print(f'CSV valido: {len(plan)} compras; nenhuma conexao realizada')
            return 0
        email, password = os.environ.get('RPA_EMAIL'), os.environ.get('RPA_PASSWORD')
        if not email or not password:
            raise ValueError('Defina RPA_EMAIL e RPA_PASSWORD da conta demo')
        args.output.mkdir(parents=True, exist_ok=True)
        lock = args.output / 'run.lock'
        try:
            fd = os.open(lock, os.O_CREAT | os.O_EXCL | os.O_WRONLY)
        except FileExistsError:
            lock = None
            raise RuntimeError('Outra execucao ou lock de interrupcao; nao executar em paralelo')
        os.close(fd)
        state_path = args.output / 'state.json'
        state = json.loads(state_path.read_text(encoding='utf-8')) if state_path.exists() else {
            'target': base, 'database': DEMO_DATABASE, 'purchases': {}}
        if state['target'] != base or state['database'] != DEMO_DATABASE:
            raise ValueError('Diario pertence a outro destino')
        from playwright.sync_api import sync_playwright
        with sync_playwright() as pw:
            browser = pw.chromium.launch(headless=not args.headed, slow_mo=200 if args.headed else 0)
            context = browser.new_context(timezone_id='America/Sao_Paulo')
            origin = urlsplit(base).netloc
            def guard_route(route):
                url = urlsplit(route.request.url)
                if url.scheme == 'http' and url.netloc == origin:
                    route.continue_()
                else:
                    route.abort()
            context.route('**/*', guard_route)
            page = context.new_page()
            page.set_default_timeout(15000)
            robot = Robot(page, base, state['purchases'], lambda: save_json(state_path, state))
            robot.login(email, password)
            robot.preflight(plan)
            for group in plan:
                try:
                    report['results'].append(robot.process(group))
                except Exception:
                    report['results'].append({'ref': group['ref'], 'status': 'failed'})
                    raise  # Stop batch: do not expand partial failures.
            browser.close()
        report['status'] = 'success'
        return 0
    except Exception as error:
        # Playwright exceptions can contain page data: don't put them in artifacts.
        report.update(status='failed', error_type=type(error).__name__)
        if isinstance(error, (ValueError, RuntimeError)):
            report['error'] = str(error)
        print('Falha: ' + (str(error) if isinstance(error, (ValueError, RuntimeError))
                          else type(error).__name__), file=sys.stderr)
        return 1
    finally:
        if not args.validate_only:
            args.output.mkdir(parents=True, exist_ok=True)
            report['finished_at'] = datetime.now().astimezone().isoformat()
            report_path = args.output / ('report-' + datetime.now().strftime('%Y%m%d-%H%M%S-%f') + '.json')
            save_json(report_path, report)
            print('Relatorio: ' + str(report_path))
        if lock is not None:
            lock.unlink(missing_ok=True)


if __name__ == '__main__':
    sys.exit(main())
