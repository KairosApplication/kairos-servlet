"""Opt-in tests against the real local Servlet demo, never a remote target."""
import json
import os
import subprocess
import sys
import tempfile
import threading
import unittest
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path

from robot import Robot, load_plan, save_json

ROOT = Path(__file__).resolve().parent


@unittest.skipUnless(os.environ.get('RPA_E2E') == 'true', 'Defina RPA_E2E=true e inicie o compose demo')
class BrowserTests(unittest.TestCase):
    def run_robot(self, output, csv=None, env=None, base=None):
        command = [sys.executable, str(ROOT / 'robot.py'), '--output', str(output)]
        if csv:
            command += ['--csv', str(csv)]
        if base:
            command += ['--base-url', base]
        result = subprocess.run(command, env=env, capture_output=True, text=True, timeout=90)
        reports = sorted(output.glob('report-*.json'))
        return result.returncode, json.loads(reports[-1].read_text(encoding='utf-8'))

    def test_replay_and_changed_csv(self):
        output = ROOT / 'output'  # Keep the same durable journal across replays.
        self.assertEqual(self.run_robot(output)[0], 0)
        before = json.loads((output / 'state.json').read_text())
        code, report = self.run_robot(output)
        self.assertEqual(code, 0)
        self.assertTrue(all(r['status'] == 'already_confirmed' for r in report['results']))
        self.assertEqual(before, json.loads((output / 'state.json').read_text()))
        with tempfile.TemporaryDirectory() as directory:
            csv = Path(directory) / 'changed.csv'
            csv.write_text((ROOT / 'compras.csv').read_text().replace(',1,2', ',1,9'))
            code, report = self.run_robot(output, csv)
            self.assertEqual(code, 1)
            self.assertIn('CSV alterado', report['error'])

    def test_invalid_login_and_missing_product(self):
        with tempfile.TemporaryDirectory() as directory:
            folder = Path(directory)
            bad_env = dict(os.environ, RPA_PASSWORD='deliberately-wrong-demo-password')
            code, report = self.run_robot(folder / 'login', env=bad_env)
            self.assertEqual(code, 1)
            self.assertIn('Autenticacao recusada', report['error'])
            csv = folder / 'missing.csv'
            csv.write_text('compra_ref,data_compra,produto_id,quantidade\nMISSING,2026-10-30T11:00,2147483647,1\n')
            code, report = self.run_robot(folder / 'missing', csv)
            self.assertEqual(code, 1)
            self.assertIn('Produtos inexistentes', report['error'])
            self.assertFalse((folder / 'missing' / 'state.json').exists())

    def test_no_credentials_sent_without_demo_confirmation(self):
        posts = []
        class Handler(BaseHTTPRequestHandler):
            def do_GET(self):
                self.send_response(200)
                self.end_headers()
                self.wfile.write(b'<form><input id="email"><input id="senha"><button>Login</button></form>')
            def do_POST(self):
                posts.append(True)
                self.send_response(500)
                self.end_headers()
            def log_message(self, *args):
                pass
        server = ThreadingHTTPServer(('127.0.0.1', 0), Handler)
        thread = threading.Thread(target=server.serve_forever, daemon=True)
        thread.start()
        try:
            with tempfile.TemporaryDirectory() as directory:
                code, report = self.run_robot(Path(directory), base=f'http://127.0.0.1:{server.server_port}')
                self.assertEqual(code, 1)
                self.assertIn('nao confirmou', report['error'])
                self.assertEqual(posts, [])
        finally:
            server.shutdown()
            server.server_close()

    def test_resume_after_interruption_between_forms(self):
        from playwright.sync_api import sync_playwright
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory)
            csv = output / 'resume.csv'
            state = {'target': 'http://127.0.0.1:8088', 'database': 'kairos_servlet_demo', 'purchases': {}}
            save = lambda: save_json(output / 'state.json', state)
            with sync_playwright() as pw:
                browser = pw.chromium.launch()
                page = browser.new_page()
                robot = Robot(page, state['target'], state['purchases'], save)
                robot.login(os.environ['RPA_EMAIL'], os.environ['RPA_PASSWORD'])
                from datetime import datetime, timedelta
                occupied = {datetime.fromisoformat(row['date']) for row in robot.purchases()}
                date = datetime(2026, 9, 1)
                while date in occupied:
                    date += timedelta(minutes=1)
                csv.write_text(f'compra_ref,data_compra,produto_id,quantidade\nRESUME,{date:%Y-%m-%dT%H:%M},2,4\n')
                plan = load_plan(csv)
                original = robot.submit
                def interrupt(form, path):
                    original(form, path)
                    if path == '/admin/compras':
                        raise RuntimeError('Simulated interruption after purchase commit')
                robot.submit = interrupt
                with self.assertRaisesRegex(RuntimeError, 'Simulated interruption'):
                    robot.process(plan[0])
                browser.close()
            code, report = self.run_robot(output, csv)
            self.assertEqual(code, 0, report)
            self.assertEqual(report['results'][0]['status'], 'confirmed')
            self.assertEqual(self.run_robot(output, csv)[0], 0)


if __name__ == '__main__':
    unittest.main()
