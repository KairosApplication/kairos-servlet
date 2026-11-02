import tempfile
import unittest
from pathlib import Path
from robot import load_plan, validate_url, fingerprint


class PlanTests(unittest.TestCase):
    def parse(self, rows):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory) / 'input.csv'
            path.write_text('compra_ref,data_compra,produto_id,quantidade\n' + rows, encoding='utf-8')
            return load_plan(path)

    def test_multiple_items(self):
        groups = self.parse('DEMO-1,2026-01-01T10:00,1,2\nDEMO-1,2026-01-01T10:00,2,3\n')
        self.assertEqual(groups[0]['items'], {1: 2, 2: 3})

    def test_invalid_batches_rejected_before_browser(self):
        for rows in ('A,2026-01-01T10:00,1,0\n',
                     'A,2099-01-01T10:00,1,1\n',
                     'A,2026-01-01T10:00,1,1\nA,2026-01-01T10:00,1,1\n',
                     'A,2026-01-01T10:00,1,1\nB,2026-01-01T10:00,2,1\n',
                     'A,2026-01-01T10:00,1,1\nA,2026-01-01T10:01,2,1\n',
                     'A,2026-01-01T10:00,999999999999,1\n',
                     '', 'A,2026-01-01T10:00,1,\n'):
            with self.subTest(rows=rows), self.assertRaises(ValueError):
                self.parse(rows)

    def test_remote_and_credential_urls_rejected(self):
        for url in ('https://aiven.io', 'http://example.com', 'http://user:pass@localhost',
                    'http://localhost?token=x', 'http://localhost/../admin'):
            with self.subTest(url=url), self.assertRaises(ValueError):
                validate_url(url)
        self.assertEqual(validate_url('http://127.0.0.1:8088/'), 'http://127.0.0.1:8088')

    def test_changed_plan_changes_fingerprint(self):
        group = self.parse('A,2026-01-01T10:00,1,1\n')[0]
        before = fingerprint(group)
        group['items'][1] = 2
        self.assertNotEqual(before, fingerprint(group))


if __name__ == '__main__':
    unittest.main()
