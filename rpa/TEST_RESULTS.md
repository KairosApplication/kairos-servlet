# Validacao efetiva — 02/11/2026

Ambiente local: Windows, Python 3.14, Playwright 1.58.0 / Chromium 145; build Docker com Maven/Java 21, Tomcat 11 e PostgreSQL 17. Nenhuma conexao Aiven, credencial real, backup ou banco principal foi usado.

## Executado

- `docker compose -f rpa/demo/compose.yml up -d --build`: WAR compilado e publicado; aplicacao real disponivel no loopback. O teste usou o namespace Docker `kairos-servlet-rpa-demo`; o compose entregue usa `kairos-servlet-demo` para manter a fixture de teste separada da instalacao do usuario. Banco e aplicacao sao os mesmos.
- `python -m unittest discover -s rpa -v` com `RPA_E2E=true`: **8 testes passaram**, repetidos apos o ajuste final do teste de retomada (19 segundos na segunda rodada).
- Primeira execucao do CSV pelo navegador: **2 compras, 3 associacoes e 3 historicos**. IDs de compras 1 e 2, FEIRA-001 com produtos 1/2 e quantidades 2/1; FEIRA-002 com produto 1 e quantidade 3.
- SELECT local independente confirmou as datas, IDs de produtos e quantidades persistidas nas tabelas `compras`, `compras_produtos` e `historico_compras_produtos`.
- Segunda execucao do mesmo CSV: `already_confirmed` para as duas compras, mesmos IDs e diario identico, sem novas associacoes.
- CSV modificado para referencia existente: recusado, sem alteracao automatica.
- Senha incorreta: login recusado e relatorio de falha.
- Produto inexistente: preflight recusou antes de cadastrar qualquer compra/diario.
- Pagina local sem header de demo: robo recusou **antes de enviar credenciais**; servidor de teste recebeu zero POSTs.
- Interrupcao simulada apos gravar a compra pelo formulario e antes de associar itens: diario ficou pendente; nova execucao retomou a mesma compra e associou o item faltante; outra repeticao foi inerte. Os testes deixam compras extras apenas no volume local de testes.
- Validacoes de CSV: quantidade zero, data futura, produto repetido, referencias com a mesma data, data divergente na mesma referencia, integer fora do limite, CSV vazio e campo ausente foram recusados.
- URLs remotas, com credenciais e com query foram recusadas.
- Container adicional com modo demo e URL `jdbc:postgresql://127.0.0.1:1/kairosdb` (destino inexistente, sem credenciais reais): GET de login retornou **503**, comprovando a recusa do datasource sem acessar banco principal.
- `git diff --check`: sem erros.

## Limites

O teste integrado realmente usou os Servlets/JSPs/Services/DAOs deste repositorio; nao foi uma simulacao de formularios. O schema da fixture foi derivado dos DAOs porque o repositorio nao inclui DDL. Ele cobre apenas os modulos necessarios ao MVP. Nao foi validada a integracao com o schema ingles do Kairos Spring/BI, pois sao modelos diferentes.

Nao foram testados acesso remoto, carga concorrente, multiempresa, isolamento de usuarios em producao ou importacao de grande volume. A idempotencia depende do diario local e de um operador por instancia; o banco original nao tem coluna de referencia externa. Falhas ambiguas param para revisao em vez de repetir gravacoes. O LoginService original usa senha em texto simples; apenas conta ficticia local foi utilizada.

Nao foram feitas exclusoes, limpeza de tabelas, alteracoes em ambientes externos ou recarga de banco existente. Os containers de teste foram parados preservando o volume local; a instalacao do usuario usa outro namespace.
