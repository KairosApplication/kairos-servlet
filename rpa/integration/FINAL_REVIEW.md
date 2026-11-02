# Auditoria final — Kairos RPA interdisciplinar

Data: **02/11/2026**, America/Sao_Paulo. Parecer: **APROVADO COM RESSALVAS** para demonstracao local controlada. A aprovacao nao se estende a producao, acesso remoto ou atualizacao visual de BI.

## A. Estado da implementacao

- Branch: `feat/rpa-normalized-integration`.
- Commit de referencia realmente inspecionado/executado: `850c3afd6c88825151f23821e825b28aff98d5bc`.
- Durante a auditoria, `origin/main` avancou para `f85ae33052c211339e6668f618ab0c0e165e6ed7`, merge da **PR #19**, feito anteriormente pelo fluxo da equipe. O agente nao fez merge na main. A branch de desenvolvimento recebeu esse commit por fast-forward antes de registrar as correcoes.
- Commit das correcoes de codigo testadas: `a9d8ecfbfefce8af66aef068471e9a191b7ddd45`. O commit posterior deste documento nao altera o codigo executavel. Para o SHA final da branch, usar `git rev-parse HEAD`.
- Kairos-data utilizado: checkout `64bccbc`; SHA-256 do schema montado e conferido com o marcador do banco: `a9a35be3db2d7aa5c6fb2ae8bad96b6412121688c43dac53daf8a25545d6d305`.

### Arquitetura confirmada no codigo e na execucao

`run.py` coordena disponibilidade dos bancos, `robot.py`, `sync.py` e reconciliacao posterior aos commits. O Playwright preenche e envia os formularios reais de login, criacao de compra e vinculacao de produto. A gravacao legada passa pelos Servlets, Services e DAOs; nao e simulada por INSERTs do teste.

O ETL reconsulta `compras`, `compras_produtos` e `produtos`, confronta os registros com o CSV/diario confirmado e importa somente esse conjunto. A extracao utiliza uma transacao `REPEATABLE READ, READ ONLY`. Apenas a identidade tecnica `rpa_source.identity` e acrescentada a origem.

O destino usa os arquivos originais do kairos-data montados read-only: schema, rotinas/triggers, views de BI e KPIs de 14 dias. Nao usa schema substituto nem dataload de um milhao. `target.sql` acrescenta somente fixture ficticia e controle de integracao.

O mapa e explicito: legado produto 1 → normalizado 10001; legado 2 → 10002; cliente ficticio 501, empresa 101. IDs de compras sao gerados pelo destino. Nome/empresa/cliente/quantidade/preco sao validados antes de confirmar cada importacao.

Compra, itens, vinculo e auditoria de sucesso compartilham uma transacao. A chave primaria `(source_instance UUID, legacy_purchase_id)`, UNIQUE no ID destino e advisory lock transacional sustentam a idempotencia. Rejeicoes recuperaveis sao auditadas apos rollback; indisponibilidade total fica no JSON, pois o banco desconectado nao pode receber auditoria.

### Correcoes minimas desta revisao

| Arquivo | Mudanca e motivo |
|---|---|
| `rpa/integration/run.py` | Encaminhar `--mapping` personalizado tambem ao `--check-only`; antes, somente a etapa final recebia esse caminho. |
| `rpa/test_e2e.py` | Permitir URL/diario exclusivos por `RPA_TEST_BASE_URL` e `RPA_TEST_OUTPUT`, mantendo os defaults anteriores; captura de subprocesso em UTF-8 no Windows. |
| `rpa/integration/test_sync.py` | Exigir exatamente uma importacao e um replay na corrida inicial, ou dois replays quando o registro ja existia; remove a possibilidade de uma assercao pouco exigente. |
| `rpa/README.md`, `rpa/integration/README.md` | Documentar o isolamento dos E2E e apontar para esta auditoria. |
| `rpa/integration/FINAL_REVIEW.md` | Registrar comandos, resultados, falha encontrada, evidencias e parecer. |

`robot.py`, `sync.py`, Java/JSP, DAOs/Services, schemas, mapa e compose nao foram alterados nesta revisao. Nao houve refatoracao de negocio.

### Falha real encontrada — resultado negativo preservado

Foi passado um JSON personalizado `{}` usando `--mapping`. No codigo original, o preflight lia o mapa padrao: disponibilidade ficava `ready`, a UI era executada e somente a sincronizacao rejeitava `invalid_import_policy`. Isso contrariava a expectativa de rejeitar a configuracao invalida antes do navegador.

- **Antes da correcao: FALHOU a expectativa do preflight.** O processo retornou 1 em `synchronization`; `playwright.status=success`. Evidencia: `pipeline-3e437147-8507-4843-895b-5d2fbd55a441.json`.
- **Depois da correcao: PASSOU.** O mesmo teste retornou 1 em `availability`, com `invalid_import_policy` e sem etapa Playwright. Evidencias: `pipeline-2b6244f5-9787-4763-a01f-2ed92742d4fc.json` e `preflight-after-fix.json`.
- Os dois retornos 1 pertencem a um teste negativo intencional. A falha anterior de ordem foi corrigida; o segundo retorno e o bloqueio esperado. Nao foram convertidos em relatos de sucesso da aplicacao.

A correcao valida a configuracao/politica selecionada antes da UI. Mapeamento ausente para um item especifico continua sendo rejeitado durante a conversao daquele registro, com atomicidade por compra, conforme a arquitetura documentada.

## B. Preparacao e testes executados

### Isolamento verificado antes de iniciar

Docker Desktop respondeu com versao **29.9.2**. Os dois repositorios estavam disponiveis; a branch estava correta e sem modificacoes locais antes da revisao. Containers e volumes existentes foram inventariados. As portas **8108 e 8109** estavam livres.

Foram usados projetos novos, sem reutilizar volumes anteriores:

- `kairos-rpa-final-review-20261102`: UI 127.0.0.1:8108, legado e normalizado novos; diario/relatorios em `rpa/output/final-review-20261102`.
- `kairos-rpa-regression-final-review`: UI 127.0.0.1:8109 e legado novo, para a suite original; diario em `rpa/output/final-review-regression`.

Credenciais ficticias foram geradas localmente, sem imprimir seus valores, e passadas apenas aos ambientes de teste. Nenhuma credencial da Aiven foi usada. Os bancos nao publicaram portas. O ambiente existente da porta 8088 nao foi reiniciado nem recebeu requisicoes destes testes.

Runtime observado: PostgreSQL **17.11**, Python Windows **3.14.6**, Playwright **1.58.0**; WAR Java 21/Tomcat 11 real e driver Psycopg 3.3.2 no container. Imagens utilizaram o build real do projeto; cache de compilacao nao substituiu a execucao dos componentes.

### Comandos de referencia da execucao observada

Todos foram executados na raiz do checkout Servlet. `--data-repo ../..` foi correto neste workspace porque o checkout Servlet fica em `kairos-data/build/kairos-servlet`. Em clones irmaos, usar `..\kairos-data`, conforme a secao F.

```powershell
$project='kairos-rpa-final-review-20261102'
$base='http://127.0.0.1:8108'
$output='rpa/output/final-review-20261102'
$env:KAIROS_DATA_PATH=(Resolve-Path ../..).Path.Replace('\','/')
$env:RPA_WEB_PORT='8108'
$dc=@('compose','-p',$project,'-f','rpa/demo/compose.yml','-f','rpa/demo/compose.integration.yml')

# A: fluxo completo em volumes novos
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ../.. --project $project --base-url $base --output $output
# C: repeticao com navegador visivel, apos a correcao
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ../.. --project $project --base-url $base --output $output --headed --no-start
# D-G/I: criar os casos pelos formularios e testar bancos reais
.\.venv\Scripts\python.exe rpa/robot.py --csv rpa/integration/cases.csv --base-url $base --output $output
docker @dc run --rm --no-deps --entrypoint python -e RPA_INTEGRATION_TESTS=true -e RPA_TEST_STATE=output/final-review-20261102/state.json -e RPA_TEST_BASE=http://127.0.0.1:8108 integration -m unittest discover -s integration -p test_sync.py -v
# H: somente a compra nova deve ser transferida
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ../.. --project $project --base-url $base --output $output --csv rpa/integration/next.csv --no-start
# Execucao final completa do README, versao corrigida, sem --no-start
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ../.. --project $project --base-url $base --output $output --headed
docker @dc exec -T normalized psql -X -U demo -d kairos_normalized_demo -v ON_ERROR_STOP=1 -f /integration/validation.sql
```

As variaveis de credenciais foram definidas antes desses comandos. Seus valores nao fazem parte deste relatorio. Os comandos da secao F mostram como prepara-las para um novo ambiente.

### Matriz A–K

| Teste | Comando/procedimento | Resultado final | Evidencia observada |
|---|---|---|---|
| **A — Fluxo completo** | Primeiro `run.py`, com subida dos servicos, RPA e ETL. | **PASSOU** | Duas compras/3 itens no legado e destino; 1→10001, 2→10002; JSON `success` e `matched`. |
| **B — Integridade** | SELECTs independentes dos dois bancos e `validation.sql`. | **PASSOU** | Tres FKs comerciais declaradas/validadas; cliente 501, empresa 101, produtos 10001/10002, quantidades corretas, status IN_PROGRESS, precos 10,00/20,00. Quatro checks SQL com zero problemas. |
| **C — Idempotencia** | Segundo `run.py --headed --no-start`, seguido de novos SELECTs. | **PASSOU** | Duas `already_imported`; manteve 2 compras/3 itens/6 unidades e os mesmos IDs. Nao houve duplicacao. |
| **D — Falha de mapa** | `test_a_missing_mapping_audited_and_recoverable`. | **PASSOU** | Remocao do mapa do produto 2 gerou `missing_product_mapping:2`; sem compra/item/vinculo parcial; tentativa rejeitada persistida na auditoria. |
| **E — Atomicidade** | `test_b_item_sql_failure_rolls_back_header_items_and_audit_trigger` e primeira parte de `test_c_connection_interruption_and_recovery`. | **PASSOU** | `SELECT 1/0` real apos INSERT do primeiro item e fechamento real da conexao apos INSERT do cabecalho. Contagens de compras/itens/vinculos/auditoria comercial permaneceram iguais apos cada rollback. |
| **F — Recuperacao** | Reprocessamento nas suites D/E com mapa/conexao restaurados. | **PASSOU** | Mesmas compras legadas importadas corretamente; destinos 10003, 10005 e 10007, sem registros incompletos anteriores. |
| **G — Concorrencia** | Duas conexoes em `ThreadPoolExecutor`, `test_e_concurrent_replay_has_one_persistent_link`. | **PASSOU** | Um `imported` e um `already_imported` para legado 8; exatamente um vinculo/compra destino 10010. SELECT separado da auditoria confirmou 1 tentativa de cada status. |
| **H — Nova compra** | `run.py --csv rpa/integration/next.csv --no-start`. | **PASSOU** | Legado 9→10011 importado; legados 1/2 retornaram already_imported. Somente uma compra e um item novos. |
| **I — Seguranca** | `test_g_main_database_and_remote_hosts_refused_before_connection`, `test_i_database_unavailability_fails_without_business_writes`, revisao dos guards e teste negativo de mapa. | **PASSOU** | `kairosdb`, `kairos_demo`, host externo e ausencia de autorizacao recusados; portas fechadas da origem/destino falharam sem gravacao. Destinos reais de producao nunca foram contatados. Mapa invalido agora para antes da UI. |
| **J — Reconciliacao independente** | `psql` com `BEGIN READ ONLY` nas duas instancias; comparacao PowerShell dos JSONs SQL, sem chamar `reconcile`/`verify_target`. | **PASSOU** | Todas as 12 linhas de itens corresponderam usando o mapa explicito. Contagens 9/9, itens 12/12, unidades 22/22, diferencas zero. Conferencia propria da aplicacao tambem retornou matched apos commits. |
| **K — Regressao** | Oito testes originais com RPA_E2E=true, URL 8109 e diario exclusivo. | **PASSOU** | 8/8 passaram, incluindo os quatro E2E anteriormente ignorados. Ambiente separado do fluxo A–J e do usuario. |

As falhas E ocorreram realmente em compras ainda nao importadas do novo volume; nao foram apenas ramificacoes de replay. Nao foram usados mocks de banco. Os patches de ambiente no teste I somente configuram destinos proibidos para exercitar a recusa real do codigo antes da conexao.

### Totais e resultados negativos

- **Cenarios obrigatorios A–K:** 11 PASSOU; 0 FALHOU pendente; 0 IGNORADO; 0 NAO EXECUTADO.
- **Suites automatizadas finais:** 9 testes de integracao + 8 testes originais = **17 aprovados, 0 falhos, 0 ignorados, 0 nao executados**. A suite de banco terminou em 0,623 s e a original em 24,584 s; sao tempos de fixture pequena, nao SLA.
- **Defeito adicional anterior a correcao:** uma expectativa de preflight falhou, foi reproduzida e corrigida. O historico negativo esta descrito acima e nao foi apagado dos artefatos.
- Builds, inicializacao, comando completo do README e `validation.sql` terminaram com codigo 0. Testes negativos intencionais de configuracao retornaram 1, conforme esperado apos a correcao.
- `git diff --check` passou. Busca em arquivos rastreados por formatos de tokens GitHub, chaves AWS, chaves privadas e DSNs com senha retornou zero arquivos correspondentes, sem imprimir conteudo de credenciais. Isso complementa a revisao manual e nao equivale a uma certificacao exaustiva de segredos.

## C. Conferencia dos bancos e evidencias

| Momento | Compras origem/destino | Itens origem/destino | Unidades origem/destino |
|---|---|---|---|
| A — primeira transferencia | 2 / 2 | 3 / 3 | 6 / 6 |
| C — repeticao | 2 / 2 | 3 / 3 | 6 / 6 |
| Final — casos recuperados + compra nova | 9 / 9 | 12 / 12 | 22 / 22 |

Foram confirmados 12 registros de historico de itens no legado, 9 vinculos persistentes no destino e zero chaves de integracao duplicadas. O ambiente separado de regressao nao faz parte dessas contagens.

| ID legado | ID normalizado | Produtos/quantidades normalizados |
|---|---|---|
| 1 | 10001 | 10001 × 2; 10002 × 1 |
| 2 | 10002 | 10001 × 3 |
| 3 | 10003 | 10001 × 2; 10002 × 1 |
| 4 | 10005 | 10001 × 1; 10002 × 2 |
| 5 | 10007 | 10002 × 4 |
| 6 | 10008 | 10001 × 1 |
| 7 | 10009 | 10002 × 1 |
| 8 | 10010 | 10001 × 2 |
| 9 | 10011 | 10002 × 2 |

Todos esses registros mantiveram a data legada, cliente 501, empresa 101, status IN_PROGRESS e origem de preco current_demo_price. Lacunas 10004/10006 sao esperadas: sequences PostgreSQL nao sao revertidas pelo rollback. Nao sao cabecalhos parciais persistidos.

### SQL independente usado na conferencia

Na origem, executado em transacao READ ONLY:

```sql
SELECT c.id AS legacy_id,c.data_compra AS date,
       cp.produto_id AS product_id,cp.quantidade_item AS quantity
FROM compras c JOIN compras_produtos cp ON cp.compra_id=c.id
ORDER BY c.id,cp.produto_id;
```

No destino, tambem READ ONLY, sem usar funcoes Python de validacao:

```sql
SELECT l.legacy_purchase_id AS legacy_id,p.id AS normalized_id,p.date,p.status,
       p.customer_id,c.company_id,pp.product_id,pp.quantity,pp.amount::numeric AS amount,
       pr.company_id AS product_company_id,l.price_origin
FROM rpa_integration.purchases l
LEFT JOIN purchases p ON p.id=l.purchase_id
LEFT JOIN customers c ON c.id=p.customer_id
LEFT JOIN purchase_products pp ON pp.purchase_id=p.id
LEFT JOIN products pr ON pr.id=pp.product_id
ORDER BY l.legacy_purchase_id,pp.product_id;

SELECT count(*) FROM pg_constraint
WHERE contype='f' AND convalidated
  AND conrelid IN ('purchases'::regclass,'purchase_products'::regclass);
```

A comparacao externa usou produto 1→10001 e 2→10002 e confrontou cada linha, data, quantidade, cliente/empresa, status e preco demonstrativo esperado. O resultado teve `differences: []`; a contagem de FKs foi 3.

`validation.sql` retornou zero em `item_company_mismatch`, `duplicate_integration_key`, `import_item_count_mismatch` e `unexpected_completed_import`. A view `vw_bi_sales_14d` ficou vazia e nao houve compra COMPLETED: o prototipo nao apresentou importacao como venda concluida.

### Artefatos locais reais

Em `rpa/output/final-review-20261102` (fora do Git, preservados na maquina):

- `integration/pipeline-1971ca09-5ef7-4c19-9bda-307b4ca3f68c.json`: A, transferencia inicial.
- `integration/pipeline-3e437147-8507-4843-895b-5d2fbd55a441.json`: defeito reproduzido antes da correcao.
- `integration/pipeline-2b6244f5-9787-4763-a01f-2ed92742d4fc.json`: mapa invalido bloqueado antes da UI apos correcao.
- `integration/pipeline-d1e822bf-d5a9-415e-a150-b035d40a2881.json`: C, repeticao com navegador visivel.
- `integration/pipeline-955edd19-1839-46b6-a95a-a03c1fcf47a5.json`: H, somente a compra nova importada.
- `integration/pipeline-ec2a45ff-2186-4380-abc4-a6e54545881f.json`: comando completo final, versao corrigida, success/matched.
- `source-A.json`, `target-A.json`, `comparison-A.json`; correspondentes `C`, `J` e `FINAL`: snapshots e comparacao SQL independente.
- `integration-tests.log`: 9 testes; `regression.log`: 8 testes sem ignorados; `preflight-after-fix.json`: assercao negativa.
- `compare.ps1`: procedimento local de comparacao independente usado nesta auditoria. Ele nao importa codigo de reconciliacao da aplicacao.

Os arquivos `failed` de mapa sao evidencias dos testes negativos; nao sao falhas remanescentes da transferencia. Credenciais demo locais e diários permanecem fora do Git. Valores de senhas/tokens nao foram copiados para este documento.

## D. Seguranca, riscos e limites

**Isolamento verificado:** nomes de bancos fixos no ETL, allowlist de hosts locais/internos, autorizacao explicita do destino, marcador da fixture e verificacao do nome real da conexao. Java confirma catalogo demo; Playwright exige URL loopback e header de demo antes de enviar credenciais. SQL de dados usa parametros. Nao houve Aiven, AWS ou banco remoto nos testes.

Todos os volumes existentes foram preservados. Nenhum DROP/TRUNCATE/DELETE de recuperacao, nenhum `down -v` e nenhuma limpeza de diario do usuario foram executados. Os containers criados para a auditoria foram parados ao final sem remover volumes. Temporarios novos da propria suite seguem o ciclo normal dos testes.

Ressalvas que precisam acompanhar a apresentacao:

1. O login legado compara senha diretamente. Foi usado somente com contas ficticias locais; nao representa autenticacao pronta para producao ou internet.
2. A idempotencia da UI depende do diario e de um operador por instancia. A etapa ETL tem chave persistente e concorrencia testada; isso nao transforma o legado em um sistema de importacao distribuida exatamente uma vez.
3. Cliente/empresa sao referencia demo explicita, nao inferencia de identidade de clientes reais. Precos atuais da fixture nao sao precos historicos. Status continua IN_PROGRESS e nao consome estoque.
4. Protecoes contra enganos nao sao barreira contra operador privilegiado que modifique codigo, DNS/roteamento ou marcadores. A aprovacao assume o compose local revisado e nao autoriza destinos arbitrarios.
5. Extracao e snapshot consistente, nao replicacao continua/bidirecional. Mudancas posteriores na origem, mapa ou destino sao rejeitadas para revisao; nao ha atualizacao silenciosa de compras ja importadas.
6. Atomicidade e por compra, nao pelo lote completo. Outros registros podem ter sido importados antes de uma rejeicao. Em perda total da conexao, o banco indisponivel nao recebe auditoria; o JSON registra a falha.
7. Schema do kairos-data e real, mas a fixture e minima. Nao prova o deploy completo/migracoes adicionais do Spring Boot ou funcionamento de todas as telas do legado.
8. Nao foram testados grande volume, rede remota, usuarios concorrentes no legado, endurecimento de seguranca, Spring Boot em execucao nem dashboards/atualizacao visual do Daniel. Esses itens nao fazem parte da aprovacao local A–K.
9. Docker, Python/Playwright, checkout do kairos-data e credenciais locais sao dependencias reais. Novos volumes precisam de credenciais novas; volumes existentes exigem as originais. Nao apagar volumes para resolver configuracao.

## E. Parecer tecnico

**APROVADO COM RESSALVAS.** O requisito local CSV → interface legada → dois bancos foi executado e comprovado por dados persistidos, testes com falhas reais, replay, corrida concorrente e SQL independente. Todos os cenarios A–K terminaram aprovados apos a correcao identificada.

Nao ha problema critico remanescente observado que impeça essa demonstracao local. A classificacao inclui as limitacoes acima: nao e certificacao de producao, nao valida autenticacao real/BI e nao autoriza concluir artificialmente as compras.

Estado Git/PR: a implementacao original foi integrada pela [PR #19](https://github.com/KairosApplication/kairos-servlet/pull/19). As correcoes desta auditoria ficam na branch de desenvolvimento e precisam de **nova PR**, sem merge automatico. O agente nao alterou diretamente a main. Usar o SHA final informado na entrega e conferir `git status --short`.

## F. Proximos passos e comandos para reproduzir

Prioridades reais:

1. Revisar e integrar por nova PR a correcao do preflight e o isolamento da suite. Nao fazer merge automatico.
2. Executar o comando da feira na maquina escolhida, com as credenciais corretas de seus volumes, e conferir o JSON. Os testes desta auditoria nao substituem conhecer a senha local dessa maquina.
3. Apresentar explicitamente cliente/preco demo e IN_PROGRESS. Somente se houver outra demanda, tratar posteriormente autenticacao de producao, acesso remoto e conexao visual do BI; nao sao bloqueios da demo local validada.

### Reproducao em ambiente novo, sem tocar na demo existente

No VS Code, abrir a raiz do `kairos-servlet` e usar PowerShell. Para clones irmaos:

```powershell
git fetch origin
git switch feat/rpa-normalized-integration
git pull --ff-only
$dataRepo=(Resolve-Path ..\kairos-data).Path
```

No workspace desta auditoria, o caminho correto foi:

```powershell
Set-Location 'C:\Users\gabin\Downloads\kairos\kairos-data\build\kairos-servlet'
$dataRepo='C:\Users\gabin\Downloads\kairos\kairos-data'
```

Selecionar somente uma das duas formas de localizar os repositorios. Depois:

```powershell
# Reutilizar a venv se ja existir; nao a apagar.
if (!(Test-Path .venv/Scripts/python.exe)) { python -m venv .venv }
.\.venv\Scripts\python.exe -m pip install -r rpa/requirements.txt
.\.venv\Scripts\python.exe -m playwright install chromium

# Conferir portas antes. Se ocupadas, escolher outro par e ajustar URLs/variaveis abaixo.
Get-NetTCPConnection -LocalPort 8108,8109 -State Listen -ErrorAction SilentlyContinue
$project='kairos-review-' + (Get-Date -Format yyyyMMddHHmmss)
$out='rpa/output/' + $project
$base='http://127.0.0.1:8108'
$env:RPA_EMAIL='review-demo@kairos.invalid'
$accountSecret=Read-Host 'Crie senha de conta apenas para este NOVO ambiente' -AsSecureString
$env:RPA_PASSWORD=[System.Net.NetworkCredential]::new('', $accountSecret).Password
$sourceSecret=Read-Host 'Crie senha do NOVO banco legado local' -AsSecureString
$env:DEMO_DB_PASSWORD=[System.Net.NetworkCredential]::new('', $sourceSecret).Password
$targetSecret=Read-Host 'Crie senha do NOVO banco normalizado local' -AsSecureString
$env:DEMO_TARGET_PASSWORD=[System.Net.NetworkCredential]::new('', $targetSecret).Password

# A e C: duas execucoes, preservando os mesmos diarios e credenciais
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo $dataRepo --project $project --base-url $base --output $out --headed
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo $dataRepo --project $project --base-url $base --output $out --headed --no-start

# Casos de falha criados pelo navegador, seguidos de nove testes de banco
.\.venv\Scripts\python.exe rpa/robot.py --csv rpa/integration/cases.csv --base-url $base --output $out
$env:KAIROS_DATA_PATH=$dataRepo.Replace('\','/')
$env:RPA_WEB_PORT='8108'
$dc=@('compose','-p',$project,'-f','rpa/demo/compose.yml','-f','rpa/demo/compose.integration.yml')
$state=('output/' + $project + '/state.json')
docker @dc run --rm --no-deps --entrypoint python -e RPA_INTEGRATION_TESTS=true -e "RPA_TEST_STATE=$state" -e "RPA_TEST_BASE=$base" integration -m unittest discover -s integration -p test_sync.py -v

# H e evidencia SQL
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo $dataRepo --project $project --base-url $base --output $out --csv rpa/integration/next.csv --no-start
docker @dc exec -T db psql -X -U demo -d kairos_servlet_demo -c 'SELECT c.id,c.data_compra,cp.produto_id,cp.quantidade_item FROM compras c JOIN compras_produtos cp ON cp.compra_id=c.id ORDER BY c.id,cp.produto_id;'
docker @dc exec -T normalized psql -X -U demo -d kairos_normalized_demo -v ON_ERROR_STOP=1 -f /integration/validation.sql

# K: outra instancia, porta e diario, sem normalizado nem alteracao no projeto anterior
$regProject=$project + '-regression'
$env:RPA_WEB_PORT='8109'
$regDc=@('compose','-p',$regProject,'-f','rpa/demo/compose.yml','-f','rpa/demo/compose.integration.yml')
docker @regDc up -d --build --wait --wait-timeout 180 db web
# Aguardar HTTP, pois container iniciado nao significa JSP pronta.
$regReady=$false
for($attempt=0;$attempt -lt 60;$attempt++) {
    try {
        $page=Invoke-WebRequest 'http://127.0.0.1:8109/login-page' -TimeoutSec 3
        if($page.Headers['X-Kairos-Demo-Database'] -eq 'kairos_servlet_demo') {
            $regReady=$true
            break
        }
    } catch {}
    Start-Sleep -Seconds 1
}
if(!$regReady) { throw 'Instancia isolada de regressao nao ficou pronta' }
$env:RPA_E2E='true'
$env:RPA_TEST_BASE_URL='http://127.0.0.1:8109'
$env:RPA_TEST_OUTPUT='rpa/output/' + $regProject
.\.venv\Scripts\python.exe -m unittest discover -s rpa -v
Remove-Item Env:RPA_E2E,Env:RPA_TEST_BASE_URL,Env:RPA_TEST_OUTPUT

# Parar somente os projetos criados por este roteiro; manter volumes e diarios.
$env:RPA_WEB_PORT='8108'
docker @dc stop
$env:RPA_WEB_PORT='8109'
docker @regDc stop
Remove-Item Env:RPA_PASSWORD,Env:DEMO_DB_PASSWORD,Env:DEMO_TARGET_PASSWORD
```

O orquestrador imprime o caminho exato do JSON consolidado. Em um ambiente novo o primeiro run deve mostrar imported; no segundo, already_imported; reconciliacao deve ser matched. Para a feira, o comando curto do README continua valido com o projeto/diario originais e suas credenciais existentes. Nao substituir essas senhas por novas esperando alterar usuarios de volumes ja populados.
