# Integracao RPA — primeiro ano para segundo ano

## Caminho rapido no VS Code / PowerShell

Nao precisa de Aiven, pgAdmin ou Spring Boot. **Nao altere `kairosdb` nem `kairos_demo`.** O fluxo usa o Servlet da PR #18 e acrescenta `kairos_normalized_demo` em outro container, sem publicar nenhuma porta PostgreSQL.

1. Tenha Docker Desktop ligado, Docker Compose 2.24.4+ (suporte a `!override`) e Python 3.10+. Use esta branch `feat/rpa-normalized-integration`. Tenha os dois repositorios locais, por exemplo:

   ```text
   kairos/
     kairos-servlet/
     kairos-data/
   ```

   O `kairos-data` precisa conter schema, rotinas, views de BI e `ScriptSQL_Kairos_Demo_KPIs.sql`. Eles sao montados **somente para leitura**, sem copiar/reescrever o schema no Servlet. Nao e executado o dataload de um milhao.

2. Abra o terminal na raiz de `kairos-servlet`. Se ainda nao instalou o robo:

   ```powershell
   python -m venv .venv
   .\.venv\Scripts\python.exe -m pip install -r rpa/requirements.txt
   .\.venv\Scripts\python.exe -m playwright install chromium
   ```

   Psycopg roda dentro do container de integracao; nao precisa instalar driver PostgreSQL na maquina.

3. Defina as credenciais locais na sessao atual. Se ja executou o RPA, use **as mesmas credenciais locais do volume existente** para a conta do Servlet e o banco legado. As senhas nao sao as da Aiven.

   ```powershell
   $env:RPA_EMAIL='feira@kairos.invalid'
   $rpaSecret = Read-Host 'Senha da conta demo do Servlet' -AsSecureString
   $env:RPA_PASSWORD = [System.Net.NetworkCredential]::new('', $rpaSecret).Password
   $sourceSecret = Read-Host 'Senha do PostgreSQL legado local' -AsSecureString
   $env:DEMO_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $sourceSecret).Password
   $targetSecret = Read-Host 'Crie uma senha apenas para o novo banco normalizado local' -AsSecureString
   $env:DEMO_TARGET_PASSWORD = [System.Net.NetworkCredential]::new('', $targetSecret).Password
   ```

   No primeiro uso, a conta e os dois produtos legados sao criados pela fixture original. Variaveis novas nao mudam senhas ja gravadas em volumes existentes. Nao apague volumes para corrigir senha; confira a configuracao utilizada na criacao.

4. Execute **um comando** para iniciar/verificar os servicos, executar o Playwright, transferir e reconciliar:

   ```powershell
   .\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ..\kairos-data --headed
   ```

   Se seus repositorios nao forem irmaos, substitua `..\kairos-data` pelo caminho real. O comando usa o projeto Docker original `kairos-servlet-demo`, a UI `http://127.0.0.1:8088`, o CSV `rpa/compras.csv` e o diario original `rpa/output/state.json`. O legado existente e preservado; apenas um novo servico/volume normalizado e adicionado. O WebServlet, JSPs, Services, DAOs e `robot.py` aprovados continuam iguais.

5. O terminal mostra `Relatorio consolidado: ...pipeline-....json`. Abra esse arquivo no VS Code. Confira:
   - `status: success`;
   - `phases.playwright`: compras confirmadas/reutilizadas pela interface;
   - `phases.synchronization.results`: `imported` ou `already_imported`, com IDs de origem/destino e itens;
   - `phases.synchronization.reconciliation`: `matched`, com quantidades iguais nos dois bancos.

6. Repita o mesmo comando. Nenhuma compra/item sera duplicado. Para somente revalidar/sincronizar compras ja confirmadas, acrescente `--skip-ui`. Depois da primeira compilacao, `--no-start` pula inicializacao/build; os containers e imagens precisam estar prontos.

7. Para mostrar uma compra nova sem alterar as anteriores:

   ```powershell
   .\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ..\kairos-data --csv rpa/integration/next.csv --headed --no-start
   ```

   Esse CSV inclui as duas compras originais mais FEIRA-003. O esperado e duas `already_imported` e uma `imported`. Os IDs reais podem diferir dos exemplos se ja houver outras compras no volume.

## O que foi implementado

Fluxo: **CSV → `robot.py`/Playwright → JSP/Servlet → `compras`/`compras_produtos` → Psycopg → `Purchases`/`Purchase_Products` → reconciliacao e auditoria**.

O orquestrador `run.py` executa as etapas em ordem e nao inicia o RPA se os bancos/configuracoes nao estiverem disponiveis. O robo aprovado continua responsavel pelas gravacoes no legado. `sync.py` recebe somente os IDs que o diario confirma para o CSV solicitado; nao importa indiscriminadamente todas as compras de teste/manual do legado. Confere novamente datas, produtos e quantidades no PostgreSQL de origem antes de importar.

O destino e inicializado com os arquivos originais de `kairos-data`: criacao de tabelas, triggers/procedures/functions, views BI e KPIs de 14 dias. O schema e suas constraints reais permanecem intactos. A fixture minima acrescenta duas empresas, dois clientes ficticios identificados e tres produtos; a segunda empresa serve para testar rejeicoes de vinculos indevidos. Nao ha estoque criado e nenhuma procedure de conclusao e chamada.

O RPA/intercambio nao foi encontrado nos caminhos de compras inspecionados no Spring Boot; seu `PurchaseService` calcula totais, e os modelos usam cliente, status, itens e MONEY. A infra inspecionada administra deploy de APIs/Redis/PostgreSQL externo, sem servico dessa transferencia no `services.json`. Nenhuma mudanca foi feita nesses dois repositorios.

## Mapeamento e politica conservadora

Configuracao explicita: `mapping.json`. IDs iguais entre bancos **nao** sao usados para inferir identidade.

| Origem | Destino | Regra |
|---|---|---|
| `compras.id` | ID gerado de `Purchases` | Vinculo persistente `(UUID da origem, ID legado)`; o ID nao e copiado. |
| `compras.data_compra` | `Purchases.date` | Timestamp sem fuso interpretado como horario de Sao Paulo, preservado sem deslocamento. |
| Nao existe cliente no legado | `customer_id = 501` | Cliente **FICTICIO DEMO**, previamente cadastrado; nao inferido de dados pessoais. |
| Nao existe empresa no legado | Empresa 101 do cliente/produtos | Todos os produtos devem pertencer a mesma empresa do cliente configurado. |
| Nao existe status de conclusao | `IN_PROGRESS` | Importacao nao comprova venda; nunca concluir automaticamente. |
| `produto_id = 1` | `product_id = 10001` | Arroz; nomes esperados dos dois lados conferidos, sem correspondencia automatica por nome. |
| `produto_id = 2` | `product_id = 10002` | Feijao; mesma politica de correspondencia explicita. |
| `quantidade_item` | `quantity` | Inteiro positivo; sem ajustes ou agregacoes silenciosas. |
| Nao existe preco no legado | `amount` unitario | **Preco atual da fixture demo**: 10,00 e 20,00. Origem `current_demo_price`, registrada no vinculo e relatorio. Nao e preco historico. |
| Historico do Servlet | Permanece na origem | Snapshot de importacao guardado no controle; nao inventar historico comercial no destino. |

Nao ha campo do legado que permita escolher um cliente real, empresa real, preco historico ou status concluido. Produto sem mapa, nome inesperado, cliente/empresa invalidos, quantidade inconsistente ou preco ausente/negativo causam rejeicao. Nao sao criados clientes/produtos novos durante a importacao.

## Idempotencia e transacoes

- A origem recebe apenas uma tabela tecnica `rpa_source.identity`, com UUID criado uma vez por volume. Nenhuma compra/item de origem e modificado pelo ETL. A extracao usa transacao `REPEATABLE READ, READ ONLY`.
- O destino guarda `rpa_integration.purchases`, com PK `(source_instance, legacy_purchase_id)`, UNIQUE no ID destino e FK para a compra. A deduplicacao da transferencia nao depende de datas, nomes ou do arquivo local.
- Advisory lock transacional serializa importacoes concorrentes da mesma chave. Compra, itens, snapshot, vinculo e auditoria de sucesso sao gravados na mesma transacao. Erro de item ou queda antes do commit reverte essa unidade inteira, inclusive o trigger de auditoria original.
- Uma nova execucao reconsulta a chave persistente. Mesmo se a resposta do commit for perdida, nao cria uma segunda compra. Mudanca da origem, mapa ou destino depois da importacao causa rejeicao e revisao manual; nunca sobrescreve silenciosamente o registro antigo.
- Precos ja importados nao sao recalculados em reexecucoes. A conferência usa o snapshot salvo na primeira importacao.
- `rpa_integration.attempts` registra run ID, UUID origem, ID legado, ID destino, horario, status, numero de itens e motivo controlado. Uma rejeicao e auditada apos rollback da compra. Indisponibilidade total do destino fica no relatorio em arquivo, pois nao e possivel gravar auditoria em banco desconectado.
- O relatorio consolidado inclui a etapa UI, a sincronizacao e uma nova conferência **apos os commits**, em transacao somente leitura. Importacoes de outras compras podem ter concluido antes de uma rejeicao: a atomicidade e por compra, nao pelo arquivo inteiro.
- A idempotencia da **interface** permanece com os limites documentados no README original: preservar diario e usar um operador. A idempotencia **entre bancos** usa UUID/ID/constraints no destino e foi testada com duas conexoes concorrentes.

## Evidencias SQL somente leitura

Na mesma sessao PowerShell, configure as variaveis auxiliares para comandos Docker diretos:

```powershell
$env:KAIROS_DATA_PATH=(Resolve-Path ..\kairos-data).Path.Replace('\','/')
$dc=@('compose','-p','kairos-servlet-demo','-f','rpa/demo/compose.yml','-f','rpa/demo/compose.integration.yml')
docker @dc exec -T db psql -U demo -d kairos_servlet_demo -c 'SELECT c.id,c.data_compra,cp.produto_id,cp.quantidade_item FROM compras c JOIN compras_produtos cp ON cp.compra_id=c.id ORDER BY c.id,cp.produto_id;'
docker @dc exec -T normalized psql -U demo -d kairos_normalized_demo -f /integration/validation.sql
```

`validation.sql` mostra os vinculos, itens, precos/origem e tentativas. Checks de inconsistencia devem retornar zero. As quantidades entre bancos tambem estao no JSON `reconciliation`. O arquivo esta montado no container; nao precisa instalar psql nem expor portas na maquina.

### Importada nao significa concluida

Todas as compras transferidas ficam `IN_PROGRESS`. `vw_bi_fact_sale_item` pode mostra-las com esse status, mas `vw_bi_sales_14d` e a view mensal contam somente `COMPLETED`, portanto **nao devem aumentar receita de vendas concluidas por esta importacao**. Nao ha consumo de estoque. Concluir uma venda futuramente exige o fluxo de negocio autorizado, que nao faz parte desta demo. O dashboard do Daniel nao foi conectado nem teve atualizacao visual testada.

## Testes reais

Os testes usam os mesmos schemas, Servlet e PostgreSQLs reais. Para nao misturar com a feira, use um projeto/porta/diario separados e novas credenciais ficticias (nao reutilize as de producao). Nao apague volumes ja existentes:

```powershell
# Com as quatro variaveis de credenciais definidas conforme acima:
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ..\kairos-data --project kairos-interdisciplinary-test --base-url http://127.0.0.1:8098 --output rpa/output/interdisciplinary-test
.\.venv\Scripts\python.exe rpa/robot.py --csv rpa/integration/cases.csv --base-url http://127.0.0.1:8098 --output rpa/output/interdisciplinary-test
$env:RPA_WEB_PORT='8098'
$env:KAIROS_DATA_PATH=(Resolve-Path ..\kairos-data).Path.Replace('\','/')
$dc=@('compose','-p','kairos-interdisciplinary-test','-f','rpa/demo/compose.yml','-f','rpa/demo/compose.integration.yml')
docker @dc run --rm --no-deps --entrypoint python -e RPA_INTEGRATION_TESTS=true -e RPA_TEST_STATE=output/interdisciplinary-test/state.json -e RPA_TEST_BASE=http://127.0.0.1:8098 integration -m unittest discover -s integration -p test_sync.py -v
.\.venv\Scripts\python.exe rpa/integration/run.py --data-repo ..\kairos-data --project kairos-interdisciplinary-test --base-url http://127.0.0.1:8098 --output rpa/output/interdisciplinary-test --csv rpa/integration/next.csv --no-start
```

Nove testes de banco cobrem mapa ausente, recuperacao, erro SQL depois do primeiro item, perda real da conexao apos cabecalho, cliente/produto de outra empresa, concorrencia, alteracao da origem, integridade, indisponibilidade dos dois bancos e recusa de destinos nao autorizados. Os casos sao criados pelo mesmo Playwright. Uma repeticao dos testes conserva os dados; casos ja importados verificam replay em vez de executar novamente a injecao de falha.

Para os testes anteriores, `python -m unittest discover -s rpa -v` continua funcionando na venv. Os E2E antigos so devem ser habilitados com `RPA_E2E=true` na instancia da porta 8088 destinada a eles; nao aponta-los a um ambiente compartilhado por engano. Resultado efetivamente executado em [TEST_RESULTS.md](TEST_RESULTS.md).

## Roteiro de 2–3 minutos

1. Mostrar o CSV e o mapa 1→10001 / 2→10002, explicando cliente demo e status conservador.
2. Executar `run.py --headed --no-start` com os containers ja preparados. Mostrar cadastro/itens no navegador.
3. Abrir o JSON e o SELECT destino: ID legado e ID normalizado diferentes, quantidades iguais, `IN_PROGRESS` e `current_demo_price`.
4. Repetir: `already_imported`, IDs preservados.
5. Executar `next.csv`: somente a nova compra recebe `imported`.

Prepare as imagens antes da apresentacao; o primeiro download/build nao cabe nesse roteiro. Os testes de falha possuem evidencias separadas e nao precisam ser repetidos ao vivo.

## Falhas, limites e encerramento

- Origem/destino indisponiveis: a etapa availability falha antes do RPA; conferir containers e senhas locais. Erros detalhados seguros ficam nos JSONs `ready`, `sync` e `pipeline`. Logs do container ajudam em falha de build/inicializacao.
- Diario do RPA ausente/incompleto: preservar e recuperar o diario original. `--skip-ui` nao ignora essa verificacao e nao importa compras manuais nao confirmadas.
- Porta ocupada: nao pare containers de outra equipe. Escolha `--project OUTRO-NOME --base-url http://127.0.0.1:OUTRA-PORTA --output rpa/output/OUTRO-NOME` desde o primeiro uso. A porta e conferida contra o container do projeto selecionado.
- Troca de schema: o SHA-256 do schema que inicializou o volume e comparado ao checkout fornecido; divergencia para para revisao. Nao ha migracao automatica nem limpeza de volume.
- Preservar juntos o volume legado (incluindo UUID), o volume normalizado e o diario UI. Um clone/restauracao que conserva UUID representa a mesma origem; para uma origem independente usar novo ambiente/volume, sem apagar o existente.
- O ETL nao usa API Spring, nao instala outro backend, nao publica os bancos, nao modifica AWS/infra/Aiven e nao altera `kairos_demo` do BI. Somente nomes de banco locais fixos e hosts internos/loopback sao aceitos; destino exige autorizacao explicita e marcador de fixture.
- Nao ha sincronizacao bidirecional, correcoes/updates/deletes automaticos, conclusao de vendas ou processamento de grande volume. Mudancas de source/mapa/target exigem revisao em vez de sobrescrita.
- Banco normalizado novo usa a fixture minima e o schema de `kairos-data`; ele nao recebe as migracoes adicionais do backend Spring. Nao apresentar essa demo como deploy completo do Spring ou integracao de todos os modulos.
- Para parar, use `docker @dc stop` com o projeto correto. **Nao use `down -v`, DROP, TRUNCATE ou DELETE.** Os volumes e os diarios permanecem disponiveis para retomar. Limpe as variaveis de senha da sessao quando terminar.

Referencia tecnica: [transacoes Psycopg 3](https://www.psycopg.org/psycopg3/docs/basic/transactions.html). As excecoes do driver sao reduzidas a tipo/SQLSTATE nos relatorios; DSNs, senhas, tokens e dados pessoais nao sao registrados.
