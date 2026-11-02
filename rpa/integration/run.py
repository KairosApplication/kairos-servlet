"""One local command: availability -> existing UI robot -> ETL -> reconciliation."""
import argparse
import hashlib
import json
import os
import subprocess
import sys
import time
import uuid
from datetime import datetime
from pathlib import Path
from urllib.request import urlopen

RPA = Path(__file__).resolve().parents[1]
REPO = RPA.parent
sys.path.insert(0, str(RPA))
from robot import load_plan, save_json, validate_url


def run(command, env):
    result = subprocess.run(command, cwd=REPO, env=env, capture_output=True,
                            text=True, encoding='utf-8', errors='replace')
    if result.returncode:
        # Never put arbitrary process output (potential credentials/SQL) in reports.
        raise RuntimeError('command_failed:' + Path(command[0]).name)


def relative(path):
    return str(path.resolve().relative_to(RPA)).replace('\\', '/')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--data-repo', type=Path, required=True)
    parser.add_argument('--csv', type=Path, default=RPA / 'compras.csv')
    parser.add_argument('--output', type=Path, default=RPA / 'output')
    parser.add_argument('--mapping', type=Path, default=RPA / 'integration/mapping.json')
    parser.add_argument('--base-url', default='http://127.0.0.1:8088')
    parser.add_argument('--project', default='kairos-servlet-demo')
    parser.add_argument('--headed', action='store_true')
    parser.add_argument('--skip-ui', action='store_true', help='Reuse a previously confirmed robot journal')
    parser.add_argument('--no-start', action='store_true', help='Containers/images must already exist')
    args = parser.parse_args()
    run_id = str(uuid.uuid4())
    report = {'run_id': run_id, 'started_at': datetime.now().astimezone().isoformat(), 'phases': {}}
    args.output = args.output.resolve()
    args.output.mkdir(parents=True, exist_ok=True)
    report_dir = args.output / 'integration'
    report_dir.mkdir(exist_ok=True)
    lock = report_dir / 'pipeline.lock'
    acquired = False
    phase = 'configuration'
    try:
        fd = os.open(lock, os.O_CREAT | os.O_EXCL | os.O_WRONLY)
        os.close(fd)
        acquired = True
        data = args.data_repo.resolve()
        base = validate_url(args.base_url)
        from urllib.parse import urlsplit
        if urlsplit(base).path not in ('', '/'):
            raise ValueError('Use a instancia ROOT do compose, sem contexto na URL')
        for key in ('RPA_EMAIL', 'RPA_PASSWORD', 'DEMO_DB_PASSWORD', 'DEMO_TARGET_PASSWORD'):
            if not os.environ.get(key):
                raise ValueError('Defina ' + key + ' com credencial exclusiva da demo')
        provenance = {}
        for name in ('ScriptSQL_Kairos_Criacao_Tabelas.sql', 'ScriptSQL_Kairos_Triggers_Procedures_Functions.sql',
                     'ScriptSQL_Kairos_bi_views.sql', 'ScriptSQL_Kairos_Demo_KPIs.sql'):
            provenance[name] = hashlib.sha256((data / 'postgres' / name).read_bytes()).hexdigest()
        report['kairos_data_files'] = provenance
        load_plan(args.csv)
        # All inputs must live under rpa/; only this directory is mounted in the ETL container.
        csv_path, mapping_path, output_path = relative(args.csv), relative(args.mapping), relative(args.output)
        env = dict(os.environ, KAIROS_DATA_PATH=str(data).replace('\\', '/'),
                   RPA_WEB_PORT=str(urlsplit(base).port or 80))
        compose = ['docker', 'compose', '-p', args.project,
                   '-f', 'rpa/demo/compose.yml', '-f', 'rpa/demo/compose.integration.yml']
        phase = 'availability'
        if not args.no_start:
            print('Iniciando Servlet e dois PostgreSQLs locais; a primeira compilacao pode demorar.', flush=True)
            run(compose + ['up', '-d', '--build', '--wait', '--wait-timeout', '180', 'db', 'web', 'normalized'], env)
            run(compose + ['build', 'integration'], env)
        ready_path = report_dir / ('ready-' + run_id + '.json')
        sync_path = report_dir / ('sync-' + run_id + '.json')
        common = ['--csv', csv_path, '--state', output_path + '/state.json', '--mapping', mapping_path,
                  '--base-url', base, '--run-id', run_id]
        try:
            run(compose + ['run', '--rm', '--no-deps', 'integration', '--check-only',
                           '--mapping', mapping_path, '--run-id', run_id,
                           '--report', relative(ready_path)], env)
        finally:
            if ready_path.exists():
                report['phases']['availability'] = json.loads(ready_path.read_text(encoding='utf-8'))
        # Do not sync into a volume initialized with a different schema than the supplied checkout.
        if report['phases']['availability']['original_schema_sha256'] != provenance['ScriptSQL_Kairos_Criacao_Tabelas.sql']:
            raise ValueError('O volume destino usa outro schema; revise sem apagar volumes existentes')
        binding = subprocess.run(compose + ['port', 'web', '8080'], cwd=REPO, env=env,
                                 capture_output=True, text=True, encoding='utf-8', errors='replace')
        if binding.returncode or binding.stdout.strip() != '127.0.0.1:' + env['RPA_WEB_PORT']:
            raise ValueError('A URL web nao corresponde ao container do projeto selecionado')
        phase = 'playwright'
        if not args.skip_ui:
            for attempt in range(60):
                try:
                    with urlopen(base + '/login-page', timeout=3) as response:
                        if response.headers.get('X-Kairos-Demo-Database') == 'kairos_servlet_demo':
                            break
                except OSError:
                    pass
                time.sleep(1)
            else:
                raise RuntimeError('web_demo_not_ready')
            print('Executando o RPA aprovado pela interface.', flush=True)
            before = set(args.output.glob('report-*.json'))
            try:
                run([sys.executable, str(RPA / 'robot.py'), '--csv', str(args.csv.resolve()), '--output',
                     str(args.output), '--base-url', base] + (['--headed'] if args.headed else []), env)
            finally:
                reports = set(args.output.glob('report-*.json')) - before
                if reports:
                    report['phases']['playwright'] = json.loads(max(reports).read_text(encoding='utf-8'))
        else:
            report['phases']['playwright'] = {'status': 'reused_confirmed_journal'}
        phase = 'synchronization'
        print('Extraindo compras confirmadas e sincronizando por transacao.', flush=True)
        try:
            run(compose + ['run', '--rm', '--no-deps', 'integration', *common, '--report', relative(sync_path)], env)
        finally:
            if sync_path.exists():
                report['phases']['synchronization'] = json.loads(sync_path.read_text(encoding='utf-8'))
        report['status'] = 'success'
        return 0
    except Exception as error:
        report.update(status='failed', failed_phase=phase,
                      reason=str(error) if isinstance(error, (ValueError, RuntimeError)) else type(error).__name__)
        print('Falha em ' + phase + ': ' + report['reason'], file=sys.stderr)
        return 1
    finally:
        report['finished_at'] = datetime.now().astimezone().isoformat()
        final_path = report_dir / ('pipeline-' + run_id + '.json')
        save_json(final_path, report)
        print('Relatorio consolidado: ' + str(final_path), flush=True)
        if acquired:
            lock.unlink(missing_ok=True)


if __name__ == '__main__':
    sys.exit(main())
