# RPA Kairos Servlet — demonstracao da feira

**Fluxo interdisciplinar completo (1o → 2o ano):** siga [integration/README.md](integration/README.md). Ele coordena este mesmo robo com a transferencia transacional para outro PostgreSQL local, criado com o schema real do `kairos-data`. O procedimento abaixo continua valido para demonstrar somente o legado.

O robo Python/Playwright faz login, abre Compras, cadastra as compras do CSV e usa o formulario Vincular para associar cada produto. Confere o resultado por uma nova leitura da tela, que passa pelo Servlet/Service/DAO, e gera relatorio JSON. O robo nao executa SQL nem chama endpoints de gravacao diretamente.

**Este projeto usa schema em portugues (`compras`, `produtos`, `compras_produtos`), diferente do Kairos Spring/PostgreSQL (`purchases`, `products`). Nao conectar ao `kairosdb` nem ao `kairos_demo` preparado para o BI.** O caminho abaixo cria outro banco, `kairos_servlet_demo`, totalmente local. Nao precisa de Aiven, backup ou pgAdmin.

## Executar no Windows / PowerShell

Pre-requisitos: Docker Desktop ligado (containers Linux), Git, Python 3.10+ e internet para baixar as dependencias na primeira execucao. A compilacao usa Java 21/Maven dentro do Docker; nao precisa instalar Java/Tomcat na maquina. Execute na raiz do repositorio `kairos-servlet`, na branch `feat/rpa-compras-demo`.

1. Crie credenciais **novas e exclusivas para a demo** na sessao PowerShell. Nao use senha Aiven ou de conta real:

   ```powershell
   $env:RPA_EMAIL='feira@kairos.invalid'
   $demoPassword = Read-Host 'Crie uma senha para a conta ficticia da demo' -AsSecureString
   $env:RPA_PASSWORD = [System.Net.NetworkCredential]::new('', $demoPassword).Password
   $dbPassword = Read-Host 'Crie outra senha para o banco local da demo' -AsSecureString
   $env:DEMO_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $dbPassword).Password
   ```

2. Inicie o banco e a aplicacao (a primeira compilacao pode demorar):

   ```powershell
   docker compose -f rpa/demo/compose.yml up -d --build
   docker compose -f rpa/demo/compose.yml logs --tail 30 web
   ```

   Aguarde a inicializacao do Tomcat. Abra [http://127.0.0.1:8088/login-page](http://127.0.0.1:8088/login-page) e confira que a tela aparece. A fixture cria a conta informada e dois produtos ficticios, IDs 1 e 2. Os dados ficam no volume local exclusivo do compose. O banco nao publica porta; a aplicacao publica apenas no loopback da maquina.

3. Instale o robo:

   ```powershell
   python -m venv .venv
   .\.venv\Scripts\python.exe -m pip install -r rpa/requirements.txt
   .\.venv\Scripts\python.exe -m playwright install chromium
   ```

4. Confira o CSV e execute vendo o navegador:

   ```powershell
   .\.venv\Scripts\python.exe rpa/robot.py --validate-only
   .\.venv\Scripts\python.exe rpa/robot.py --headed
   ```

   Resultado esperado no primeiro uso: duas compras, tres associacoes e tres historicos persistidos. O robo termina com codigo 0 e informa o caminho de `rpa/output/report-*.json`. O primeiro relatorio confirma FEIRA-001 com dois produtos e FEIRA-002 com um. Nao e necessario clicar durante a execucao.

5. Rode novamente o mesmo comando: o relatorio deve mostrar `already_confirmed`, mantendo os mesmos IDs, sem duplicar compras ou itens.

6. Para parar sem perder o diario nem os dados:

   ```powershell
   docker compose -f rpa/demo/compose.yml stop
   Remove-Item Env:RPA_PASSWORD, Env:DEMO_DB_PASSWORD
   ```

   Para retomar, defina as mesmas credenciais e execute `docker compose -f rpa/demo/compose.yml start`. A conta so e criada na primeira inicializacao do volume: trocar a variavel depois nao troca a senha gravada. Nao execute `down -v` para repetir a demonstracao; preserve o volume e o diario juntos.

## CSV e demonstracao

Formato obrigatorio, UTF-8, separador virgula:

```csv
compra_ref,data_compra,produto_id,quantidade
FEIRA-001,2026-11-01T10:01,1,2
FEIRA-001,2026-11-01T10:01,2,1
FEIRA-002,2026-11-01T10:02,1,3
```

Linhas com a mesma referencia formam uma compra com varios produtos. Data sem fuso, em Sao Paulo, ate o minuto; deve estar no passado. IDs/quantidades: inteiros positivos ate o limite Java integer. Maximo de 100 itens por arquivo. Mesmo produto nao pode repetir na compra. Referencias diferentes nao podem usar a mesma data/minuto. As regras do Service continuam sendo executadas.

Para mostrar novas compras na feira, crie um CSV com referencias e minutos novos, ja passados, e execute `python rpa/robot.py --csv CAMINHO --headed` usando o Python da venv. Nao altere um CSV ja processado mantendo a mesma referencia. Os produtos devem existir na tela Produtos. O robo verifica todos os produtos antes da primeira compra. Nao existe preco/valor/status/cliente no formulario atual; o MVP nao inventa esses campos e nao atualiza os KPIs do outro projeto automaticamente.

Roteiro curto: mostrar CSV → executar `--headed` → abrir Compras e Detalhes → mostrar os itens → mostrar relatorio → repetir e mostrar ausencia de duplicacao.

## Confirmar diretamente no PostgreSQL local (somente leitura)

```powershell
docker compose -f rpa/demo/compose.yml exec -T db psql -U demo -d kairos_servlet_demo -c 'SELECT c.id, c.data_compra, cp.produto_id, cp.quantidade_item FROM compras c JOIN compras_produtos cp ON cp.compra_id=c.id ORDER BY c.id, cp.produto_id;'
```

## Seguranca e limites da idempotencia

- `KAIROS_RPA_DEMO=true` habilita a trava do datasource: exige URL e catalogo reais `kairos_servlet_demo`. O filtro publica o nome verificado no header e exige login para `/admin/*` no modo demo. O robo confirma esse header antes de enviar a senha. URL remota e recursos de outra origem sao recusados.
- O `LoginServlet` agora registra a autenticacao na sessao, com renovacao da sessao; no modo demo redireciona a Compras. Fora do modo demo, o destino original Usuarios permanece. A protecao do filtro e opt-in, nao e uma revisao completa da seguranca do aplicativo.
- O LoginService original compara senhas diretamente. A fixture acompanha essa implementacao somente com contas ficticias locais. Nao expor essa instancia na internet nem reutilizar credenciais reais.
- `rpa/output/state.json` guarda referencia → fingerprint → ID. Um lock impede duas execucoes usando o mesmo diretorio. Preserve esse diario e use **um operador/robo por instancia**, sem criacoes manuais concorrentes na mesma data. O schema nao tem chave externa de importacao: o MVP usa data/minuto e diario para reconciliar; nao promete exatamente uma gravacao sob concorrencia arbitraria.
- Sem diario, uma data ocupada e recusada, nunca adotada automaticamente. Mais de uma compra na data, ID divergente, quantidade divergente ou CSV alterado tambem param a execucao. Nao apague o diario para forcar reenvio.
- Diario e salvo antes do envio. Se a compra foi gravada e houve interrupcao antes dos itens, nova execucao reconhece a compra pela data e retoma os itens faltantes. Se o resultado do envio for incerto e nao houver compra visivel, o robo para para revisao manual em vez de reenviar cegamente. Nenhum registro e apagado para compensar falhas.
- Se houver `run.lock` apos desligamento forcado, confirme no Gerenciador de Tarefas que nao ha outro robo ativo antes de remover **somente esse arquivo**. O diario deve permanecer. Use sempre o mesmo `--base-url` e `--output`.
- Relatorios mostram IDs/referencias/status e erros controlados, sem senhas, cookies ou HTML. O robo nao salva screenshots/trace da conta. `rpa/output`, `.env` e venv ficam fora do Git.
- O schema demo e deliberadamente limitado a compras/produtos/autenticacao. Outras telas administrativas podem depender de tabelas fora da fixture. Nenhum modulo de negocio foi reimplementado; nao usar essa fixture como migracao do sistema completo.

## Testes

```powershell
.\.venv\Scripts\python.exe -m unittest discover -s rpa -v
# Com a instancia demo iniciada e as variaveis de login definidas:
$env:RPA_E2E='true'
.\.venv\Scripts\python.exe -m unittest discover -s rpa -v
Remove-Item Env:RPA_E2E
```

Para executar os E2E em outra instancia local sem afetar o diario da demonstracao, defina tambem `RPA_TEST_BASE_URL` (ex.: `http://127.0.0.1:8109`) e `RPA_TEST_OUTPUT` (ex.: `rpa/output/regression-isolated`). A instancia precisa estar iniciada nessa porta, com as credenciais demo de `RPA_EMAIL`/`RPA_PASSWORD`. Os defaults anteriores continuam disponiveis quando essas variaveis nao sao definidas.

Testes E2E usam o navegador real e deixam uma compra adicional de retomada na instancia descartavel. Nao executam exclusoes. O diario do CSV exemplo permanece em `rpa/output`. Resultados efetivamente executados em [TEST_RESULTS.md](TEST_RESULTS.md).

## Problemas comuns

- Porta 8088 ocupada: ajuste apenas o binding local no compose e passe a mesma URL com `--base-url`; use um diario novo somente se for outra instancia de banco vazia.
- Login recusado: use a senha definida na primeira criacao do volume; a senha atual da Aiven nao tem relacao com essa conta.
- Banco demo indisponivel / erro 503: confira DB_URL, driver e `KAIROS_RPA_DEMO`, nunca desative a trava para apontar ao banco principal.
- Nao confirmou banco demo: iniciou uma versao antiga ou instancia sem o modo demo. Nenhuma senha ou compra e enviada pelo robo nesse caso.
- Produto inexistente ou CSV invalido: corrija antes de executar; o lote inteiro e validado antes de gravacoes.
- Para outra maquina da equipe, reproduza o compose local. O acesso por rede a uma instancia compartilhada nao faz parte deste MVP, que aceita somente loopback. Projetar com seguranca esse acesso exige revisar autenticacao, TLS e concorrencia.

Referencias oficiais: [Playwright Python](https://playwright.dev/python/docs/intro), [espera por respostas](https://playwright.dev/python/docs/network), [deploy de WAR no Tomcat 11](https://tomcat.apache.org/tomcat-11.0-doc/deployer-howto.html).
