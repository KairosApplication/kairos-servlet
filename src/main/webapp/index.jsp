<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kairos</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <script src="${pageContext.request.contextPath}/assets/js/index.js" defer></script>

</head>
<body>

<nav class = "header">
    <a href="#Funcionalidades" class = "montserrat-unifiquifer" id="Dec"> Funcionalidades </a>
    <a href="Mapa" class = "montserrat-unifiquifer" > Mapa </a>
    <a href="Sobre" class = "montserrat-unifiquifer" > Sobre </a>
    <a href="Contato" class = "montserrat-unifiquifer"> Contato</a>
    <a href="${pageContext.request.contextPath}/login-page" class = "montserrat-unifiquifer" id="Icone-de-entrada" >  Entrar <img src="Assets/Vector.png" alt="Icone de pessoa"> </a>
</nav>
<div>
    <img src="Assets/Group 6.svg" alt="logo do kairos" class="imagem">
</div>

<div class="Kaico">
    <span>Conheça o </span>
    <span id = "Kai">KAIROS</span>
</div>
<br>
<section class="inicial">
    <div class="Geren">
        <span>Gerencie produtos, estoque e reposição</span>
        <span id = "um-só-local"> <br> um só local</span>
    </div>
    <br>
    <div class = "Topicos">
        <span><span>●</span> Controle Inteligente <br></span>
        <span><br><span>●</span> Atualização em Tempo Real <br></span>
        <span><br><span>●</span> Aplicativo para repositores</span>
    </div>
    <br>
    <div class = "Meu-Botão">
        <button id = "Comece"> Comece Grátis</button>
        <button id = "Ver-uma"> Ver Uma Demo</button>
    </div>
    <br>
    <br>
</section>
<section class="Funcionalidades">
    <section class="circulosearcos">
        <br>
        <br>
        <div class="circuloamar"></div>
        <div class="circulover"></div>
        <br>
        <br>
        <br>
        <br>
        <div class="arcoesqu"></div>
        <div class="arcodire"></div>
    </section>
    <div class="Funcional">
        <h2>Funcionalidade<div class="linhas"></div></h2>
    </div>

    <div class="conteudo">
        <section class="cards">
            <div class="card">
                <div id = "icone"> <img class="imagemcard" src="Assets/Vector.svg" alt="icone de mapa"></div>
                <h3 id="Mapa">Mapa <br> do Mercado</h3>
            </div>
            <div class="card">
                <div id = "icone"> <img class="imagemcard" src="Assets/Vector (1).svg" alt="icone de Relatório"></div>
                <h3 id="Relatório de IA">Relatório de <br> IA</h3>
            </div>
            <div class="card">
                <div id = "icone"> <img class="imagemcard" src="Assets/Vector (2).svg" alt="icone de Alertas"></div>
                <h3 id="Alertas em Tempo real">Alertas <br> em Tempo real </h3>
            </div>
            <div class="card">
                <div id = "icone"> <img class="imagemcard" src="Assets/Vector (3).svg" alt="Icone de Integração"></div>
                <h3 id="Integração da equipe">Integração da <br> equipe</h3>
            </div>
        </section>
</section>
<br>
<br>
<br>
<br>
<br>
<br>

<div class="PassoaPasso">

    <h2>
        Passo a Passo
        <div class="linhas"></div>
    </h2>

    <div class="Carrossel">

        <div class="carrossel" id="carrossel">

            <div class="Cadrastro">
                <div class="Background"></div>

                <section class="Passos">
                    <div></div>
                    <div class="bolacarrosel"></div>
                    <img src="Assets/Vector (17).svg" alt="">
                    <br>
                    <br>

                    <p id="titulocadastro">Cadastro do Mercado</p>

                    <br>

                    <p id="CadastrePS">
                        Cadastre seu mercado, insira suas informações <br>
                        e acesse sua dashboard principal
                    </p>
                </section>
            </div>


            <div class="Acesse">
                <div class="Background1"></div>

                <section class="Dash">
                    <div></div>
                    <div class="bolacarrosel"></div>
                    <img src="Assets/Vector (18).svg" alt="">
                    <br>
                    <br>

                    <p id="titulocadastro">Acesse a dashboard</p>

                    <br>

                    <p id="CadastrePS">
                        Após o cadastro, acesse a dashboard do <br>
                        sistema, e personalize como quiser!
                    </p>
                </section>
            </div>


            <div class="Perso">
                <div class="Background2"></div>

                <section class="Persona">
                    <div></div>
                    <div class="bolacarrosel"></div>
                    <img src="Assets/Vector (15).svg" alt="">
                    <br>
                    <br>

                    <p id="titulocadastro">Personalize o Mapa</p>

                    <br>

                    <p id="CadastrePS">
                        Com sua dashboard aberta, acesse a area do mapa <br>
                        e assim personalize com as escalações disponiveis
                    </p>
                </section>
            </div>


            <div class="Rece">
                <div class="Background3"></div>

                <section class="Receba">
                    <div></div>
                    <div class="bolacarrosel"></div>
                    <img src="Assets/Vector (16).svg" alt="">
                    <br>
                    <br>

                    <p id="titulocadastro">Receba os relatórios</p>

                    <br>

                    <p id="CadastrePS">
                        Com tudo personalizado, receba seus relatórios mensais <br>
                        de IA sobre o retrospecto do seu mercado
                    </p>
                </section>


            </div>

        </div>

        <div class="indicadores">
            <button id="1bot" onclick="mudarCard(0)" >1</button>
            <br>
            <button id="2bot" onclick="mudarCard(1)">2</button>
            <br>

            <button id="3bot" onclick="mudarCard(2)">3</button>
            <br>
            <button id="4bot" onclick="mudarCard(3)">4</button>
            <div class="linhacard"></div>
        </div>
        <br>

    </div>

</div>
<section class = "mapa">
    <div>
        <span class="direita">Mapa <span class="corzinha">completo</span></span>
        <br>
        <span class="direita">e <span class="corzinha">interativo</span></span>
    </div>
    <p class="frasemapa">
        Tenha uma visão clara das
        <br>gondolas do seu mercado.
        <br>Com informações atualizadas
        <br>em tempo real!
    </p>
    <br>
    <br>
    <p>Controle do estoque</p>
    <p>Informações ao vivo</p>
    <p>Veja prateleiras vazias</p>
    <button class="Demo">Ver uma Demo</button>
</section>
<br>
<br>
<section class="Mercado">
    <!-- Use H2 para títulos de seção em vez de <p> -->
    <h2 class="Beneficios">Benefícios para o seu Mercado</h2>

    <!-- Tudo que pertence ao cartão fica DENTRO dele -->
    <div class="CardMercado">

        <!-- Grupo 1 -->
        <div class="item-beneficio1">
            <img src="Assets/Vector(5).svg" alt="Casinha com Lampada" id="casinhalam">
            <p id="reduzaper">Reduza perdas <br> e desperdícios</p>
        </div>

        <div class="barra"></div>

        <!-- Grupo 2 -->
        <div class="item-beneficio1">
            <img src="Assets/Vector (4).svg" alt="Carinha com HeadFone" id="Casinha">
            <p>Monitore seu <br> Mercado</p>
        </div>

        <div class="barra"></div>

        <div class="item-beneficio1">
            <img src="Assets/Vector (6).svg" alt="Computador" id="Casinha">
            <p>Receba relatórios <br> mensais de IA</p>
        </div>

        <div class="barra"></div>

        <div class="item-beneficio1">
            <img src="Assets/Vector (7).svg" alt="Folha com lapis" id="Casinha">
            <p>Cadastre seus produtos <br> com facilidade</p>
        </div>

    </div>
</section>
<section class="valores">
    <div class="bolasvalor">
        <div class="bolavalores"></div>
        <div class="arovalores"></div>
    </div>
    <!-- cantos decorativos amarelos (fundo) -->
    <div class="valor-canto canto-sup" aria-hidden="true"></div>
    <div class="valor-canto canto-inf" aria-hidden="true"></div>
    <p class="nossovalo">Nossos Valores</p>
    <div class="linhahorisontal"></div>
    <p class="textinho">São os princípios que guiam nossas decisões, nossa cultura <br> e nosso dia dia</p>
    <div class="carrosselvalores valores-palco">
        <div class="valor-trilho" id="trilhoValores">
            <!-- ABAS DE TRÁS (giratórias, mudam de cor via JS) -->
            <div class="valor-abas" aria-hidden="true">
                <div class="valor-aba aba-tras" id="abaTras"></div>
                <div class="valor-aba aba-meio" id="abaMeio"></div>
            </div>
            <!-- CARD 1 : FOCO -->
            <section class="focos valor-card valor-verde is-ativo">
                <div class="valor-texto">
                    <p class="Foco valor-titulo">Foco</p>
                    <p id="textinhovalores" class="valor-desc">Direcionar a mente e os <br> esforços para um objetivo <br> específico, eliminando <br> distrações.</p>
                </div>
                <!-- COLOQUE SUA IMAGEM DO FOCO NO src ABAIXO -->
                <img class="valor-foto" id="img-foco" src="" alt="Imagem do valor Foco">
            </section>
            <!-- CARD 2 : PERSISTÊNCIA -->
            <section class="Persistencia valor-card valor-preto">
                <div class="valor-texto">
                    <p class="persi valor-titulo">Persistência</p>
                    <p id="textinhovalores" class="valor-desc">É a constância em buscar <br> um resultado mesmo <br> quando o caminho se torna <br> difícil.</p>
                </div>
                <!-- COLOQUE SUA IMAGEM DA PERSISTÊNCIA NO src ABAIXO -->
                <img class="valor-foto" id="img-persistencia" src="" alt="Imagem do valor Persistência">
            </section>
            <!-- CARD 3 : ESPERANÇA -->
            <section class="Esperança valor-card valor-amarelo">
                <div class="valor-texto">
                    <p class="espe valor-titulo">Esperança</p>
                    <p id="textinhovalores" class="valor-desc">É a vontade e a crença de <br> que uma situação difícil vai <br> melhorar.</p>
                </div>
                <!-- COLOQUE SUA IMAGEM DA ESPERANÇA NO src ABAIXO -->
                <img class="valor-foto" id="img-esperanca" src="" alt="Imagem do valor Esperança">
            </section>
        </div>
        <div class="valor-nav">
            <button class="passar1 valor-btn valor-btn-amarelo" type="button" data-valor-dir="-1" aria-label="Valor anterior">←</button>
            <button class="passar2 valor-btn valor-btn-verde" type="button" data-valor-dir="1" aria-label="Próximo valor">→</button>
        </div>
        <div class="bolavalores1"></div>
        <div class="arovalores1"></div>
    </div>
</section>
<footer class="rodape">
    <div class="iniciorodape"></div>
    <div class="rodape-conteudo">

        <div class="coluna col-1">
            <img class="logo-rodape" src="Assets/Group 77.svg" alt="logo do Kairos">
            <p class="texto-gerencie">
                Gerencie produtos,<br>
                estoque e reposição<br>
                em <span class="destaque">um só local!</span>
            </p>

            <div class="beneficios-grid">
                <div class="item-beneficio">
                    <img src="Assets/Vector (13).svg" alt="Casinha com Lampada">
                    <p>Reduza perdas<br>e desperdícios</p>
                </div>
                <div class="item-beneficio">
                    <img src="Assets/Vector (4).svg" alt="Carinha com HeadFone">
                    <p>Monitore seu<br>Mercado</p>
                </div>
                <div class="item-beneficio item-largo">
                    <img src="Assets/Vector (14).svg" alt="Computador">
                    <p>Receba relatórios<br>mensais de IA</p>
                </div>
            </div>
        </div>

        <div class="barra-vertical"></div>

        <div class="coluna col-2">
            <h1>Nossas redes</h2>
                <div class="barra-horizontal"></div>
                <p class="texto-descritivo">
                    <span class="destaque">Conecte-se</span> com o Kairos em<br>
                    todas as redes socias e se<br>
                    <span class="destaque">mantenha atualizado!</span>
                </p>

                <div class="lista-contatos">
                    <a href="https://instagram.com/kairos.app_" target="_blank">
                        <img src="Assets/Vector(12).svg" alt="Logo do instagram">
                        <span>kairos.app_</span>
                    </a>
                    <a href="tel:+5511934891608">
                        <img src="Assets/Vector (10).svg" alt="Logo do Whatsapp">
                        <span>+55 11 93489-1608</span>
                    </a>
                    <a href="mailto:kairos.intelligent@gmail.com">
                        <img src="Assets/Vector (11).svg" alt="Logo do email">
                        <span>kairos.intelligent@gmail.com</span>
                    </a>
                </div>
        </div>

        <div class="barra-vertical"></div>

        <div class="coluna col-3">
            <span class = "perguntasrodape">Perguntas Frequentes
            </span>
            <div class="barra-horizontal1   "></div>
            <p class="texto-descritivo">
                As principais <span class="destaque">dúvidas</span> que nossos
                usuarios tem ao utilizar o Kairos
            </p>
            <button class="btn-duvidas">Abrir dúvidas</button>
        </div>

    </div>

    <!-- Faixa escura inferior com direitos autorais -->
    <div class="rodape-inferior">
        <p>Kairos 2026 Todos os direitos reservados</p>
    </div>
</footer>
</body>
</html>
