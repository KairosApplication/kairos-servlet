# Evidencias executadas em 02/11/2026

## Ambiente e codigo inspecionado

- Servlet: base `348578c` (merge da PR #18). `robot.py`, DAOs, Services, JSPs, fixture e compose aprovados foram inspecionados e preservados; somente um link foi acrescentado ao README do RPA.
- Kairos-data: checkout `64bccbc`, schema original SHA-256 `a9a35be3db2d7aa5c6fb2ae8bad96b6412121688c43dac53daf8a25545d6d305`. Schema, rotinas/triggers, dataload, validacoes e views foram revisados. Somente schema/rotinas/views foram montados e executados; nenhum dataload grande foi usado.
- Spring Boot: checkout `a1d0d84`, modelos Purchase/PurchaseProduct, PurchaseService e migrations inspecionados. Nenhuma transferencia do legado foi encontrada nesses caminhos; nao foi necessario iniciar ou alterar o backend.
- Infra: README e services.json inspecionados; catalogo de APIs/deploy sem servico ETL legado. Sem alteracoes de infra/AWS.
- Teste em novo projeto Docker `kairos-interdisciplinary-test`, porta UI 8098, bancos `kairos_servlet_demo` e `kairos_normalized_demo`, volumes novos e distintos dos ambientes do usuario. PostgreSQL 17, Servlet/Tomcat 11/Java 21 real, Chromium/Playwright 1.58, Psycopg 3.3.2 no container Python 3.12. Nenhuma porta PostgreSQL publicada.

## A–H: resultados observados

| Teste | Resultado efetivamente executado |
|---|---|
| A — transferencia inicial | `run.py` iniciou os servicos, validou bancos, chamou o `robot.py` original e importou 2 compras/3 itens. Legado IDs 1/2 → destino 10001/10002. |
| B — integridade | Produtos destino 10001/10002, cliente 501 e empresa 101; FKs reais ativas. Checks de empresa, contagem de itens, status e duplicacao retornaram zero problemas. |
| C — repeticao | Nova execucao completa pela UI e ETL retornou `already_imported` para as mesmas compras, sem duplicar cabecalhos ou itens. |
| D — falha controlada | Produto 2 sem mapa foi rejeitado e auditado como `missing_product_mapping:2`; contagens de compras, itens, vinculos e auditoria comercial ficaram iguais. Cliente/produto de outra empresa tambem foram recusados. |
| E — recuperacao | Erro SQL real (`SELECT 1/0`) apos o primeiro item reverteu cabecalho, item e auditoria comercial. Fechamento real da conexao apos INSERT do cabecalho tambem nao deixou registro parcial. Novas conexoes importaram ambas as compras corretamente. Duas conexoes concorrentes produziram somente um vinculo/compra para a mesma chave. |
| F — nova compra | `next.csv` criou pela interface a compra legada 9; destino 10011. O mesmo run retornou `already_imported` para 1/2 e `imported` somente para 9. |
| G — conferencia | Relatorio consolidado e SELECTs independentes dos dois bancos confirmaram produtos e quantidades. Estado final dos testes: **9 compras, 12 itens, 22 unidades em cada banco**, 9 vinculos persistentes. Reconciliacao apos commits: `matched`. |
| H — seguranca | Nomes `kairosdb`/`kairos_demo`, host externo e falta de autorizacao foram recusados antes da conexao. Indisponibilidade de ambas as conexoes foi testada com porta local fechada e nao alterou contagens. Nenhuma tentativa de conexao a producao foi feita. |

## Exemplos SQL observados

| Legado | Destino | Produtos/quantidades destino | Status / cliente / empresa |
|---|---|---|---|
| 1 | 10001 | 10001 × 2; 10002 × 1 | IN_PROGRESS / 501 / 101 |
| 2 | 10002 | 10001 × 3 | IN_PROGRESS / 501 / 101 |
| 4 (erro de item recuperado) | 10005 | 10001 × 1; 10002 × 2 | IN_PROGRESS / 501 / 101 |
| 5 (conexao interrompida recuperada) | 10007 | 10002 × 4 | IN_PROGRESS / 501 / 101 |
| 9 (nova compra) | 10011 | 10002 × 2 | IN_PROGRESS / 501 / 101 |

Lacunas na sequence (10004/10006) sao esperadas: PostgreSQL nao reverte sequences no rollback. Nao representam compras persistidas parcialmente.

Precos unitarios observados: 10,00 para 10001 e 20,00 para 10002, todos com `price_origin=current_demo_price`, nunca apresentados como historicos. `vw_bi_sales_14d` permaneceu vazia, zero compras COMPLETED e zero registros de estoque; a importacao nao concluiu vendas.

## Comandos e execucoes

- `python rpa/integration/run.py ...` (primeira inicializacao e repeticoes completas): codigo 0.
- `python rpa/robot.py --csv rpa/integration/cases.csv ...`: casos reais criados pela UI, codigo 0.
- `docker compose ... run --entrypoint python ... integration -m unittest discover -s integration -p test_sync.py -v`: **9 testes passaram**. A primeira rodada de 8 testes executou efetivamente as falhas transacionais em registros ainda nao importados; a rodada final de 9 acrescentou indisponibilidade e confirmou replay dos registros recuperados.
- `python -m unittest discover -s rpa -v`: **4 testes originais passaram; 4 E2E antigos foram explicitamente pulados**, pois exigem a porta 8088, ocupada por um ambiente do usuario que nao foi modificado. A automacao real do robo foi exercitada pelo novo fluxo na porta 8098.
- `validation.sql` executado com `ON_ERROR_STOP=1`: codigo 0; quatro checks retornaram zero problemas.
- Inicializador normalizado final, incluindo trava antes de qualquer DDL original, repetido em **outro volume novo**, projeto `kairos-normalized-init-check`: healthy. Nenhum volume existente foi recarregado ou removido.
- `git diff --check`: sem erros. `git diff origin/main -- rpa/robot.py src/main/java src/main/webapp rpa/demo/compose.yml rpa/demo/schema.sql`: vazio.

Relatorios locais gerados, fora do Git por politica de artefatos:

- `pipeline-69f05df6-b069-4230-b1e7-45fa6b303402.json`: primeira transferencia 1→10001, 2→10002.
- `pipeline-5606cb1b-2617-45ed-aa9c-14fdd38b584b.json`: repeticao sem duplicacoes.
- `pipeline-fca8468d-50c1-4392-9b99-db1585ee5953.json`: somente a nova compra 9→10011 e reconciliacao das tres compras do CSV.
- `pipeline-fbb3bf64-9163-4221-9b50-0d50c3271054.json`: revalidacao dos seis casos com commits ja persistidos, sem executar a UI novamente.

O tratamento UTF-8 da captura de subprocessos no Windows foi corrigido apos um aviso de decodificacao do build Docker na primeira execucao; os passos de transferencia tinham concluido, e as repeticoes finais passaram sem esse aviso.

## Limites

Nao foram testados dashboards/atualizacao visual, Spring Boot em execucao, producao, rede remota, grande volume ou sincronizacao bidirecional. O schema usado e o real de kairos-data, nao uma simulacao, mas nao inclui migracoes extras do Spring Boot. Os testes usam apenas fixture ficticia e nao comprovam identidade comercial de produtos reais.

Os containers exclusivos dos testes foram parados preservando todos os volumes. Containers e volumes preexistentes do usuario, incluindo o Servlet da porta 8088, permaneceram intactos.
