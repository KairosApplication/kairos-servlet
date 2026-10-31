<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Kairos</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/images/favicon.png">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
</head>

<body>

<header class="cabecalho">
    <div class="wrap cabecalho__conteudo">

        <a href="#" class="logo" aria-label="Kairos - início">
            <img src="${pageContext.request.contextPath}/assets/images/logo-kairos.png" alt="" width="50" height="72">
            <span>KAIROS</span>
        </a>

        <input class="escondido" type="checkbox" id="menu-abrir">

        <label class="menu__botao" for="menu-abrir" aria-label="Abrir menu">
            <span></span>
            <span></span>
            <span></span>
        </label>

        <nav class="menu">
            <a href="#funcionalidades">Funcionalidades</a>
            <a href="#passos">Passos</a>
            <a href="#mapa">Mapa</a>
            <a href="#valores">Valores</a>

            <a href="${pageContext.request.contextPath}/login-page" class="menu__entrar">
                Entrar
                <img src="${pageContext.request.contextPath}/assets/images/usuario.svg" alt="" width="48" height="48">
            </a>
        </nav>

    </div>
</header>


<section class="hero">
    <div class="wrap hero__conteudo">

        <div class="hero__texto">

            <h1>Conheça o <b>KAIROS</b></h1>

            <p class="hero__sub">
                Gerencie produtos, estoque e reposição <br>
                em <b>um só local!</b>
            </p>

            <ul class="lista-check">

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Controle Inteligente
                </li>

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Atualização em Tempo Real
                </li>

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Aplicativo para repositores
                </li>

            </ul>

            <div class="hero__botoes">
                <a href="${pageContext.request.contextPath}/cadastro-page" class="botao botao--verde">Criar conta</a>
                <a href="#mapa" class="botao botao--borda">Ver uma Demo</a>
            </div>

        </div>

        <div class="hero__visual">

            <img class="hero__imagem"
                 src="${pageContext.request.contextPath}/assets/images/hero-repositor.svg"
                 alt="Repositor colocando produtos em uma gôndola de supermercado"
                 width="650"
                 height="520">

        </div>

    </div>
</section>


<section class="funcionalidades" id="funcionalidades">

    <svg class="deco deco--esquerda" viewBox="0 0 460 620" aria-hidden="true">

        <path d="M0 0 C150 40 270 190 270 330 C270 470 150 590 0 620Z" fill="#E8B333"/>

        <path d="M0 60 C180 50 400 110 450 255" fill="none" stroke="#1B4D3E" stroke-width="1.2"/>

        <path d="M370 435 C280 510 120 545 0 540" fill="none" stroke="#1B4D3E" stroke-width="1.2"/>

    </svg>

    <svg class="deco deco--direita" viewBox="0 0 460 620" aria-hidden="true">

        <path d="M460 0 C310 40 190 190 190 330 C190 480 310 610 460 630Z" fill="#1B4D3E"/>

        <path d="M10 255 C80 120 270 30 460 28" fill="none" stroke="#E8B333" stroke-width="1"/>

        <path d="M80 440 C160 520 300 570 460 575" fill="none" stroke="#E8B333" stroke-width="1"/>

    </svg>

    <div class="wrap">

        <h2 class="titulo-secao">Funcionalidades</h2>

        <div class="cards">

            <div class="card">

                <div class="card__icone">
                    <img src="${pageContext.request.contextPath}/assets/images/func-mapa.svg" alt="" width="54" height="54">
                </div>

                <h3>Mapa <br>do Mercado</h3>

            </div>

            <div class="card">

                <div class="card__icone">
                    <img src="${pageContext.request.contextPath}/assets/images/func-relatorio.svg" alt="" width="52" height="52">
                </div>

                <h3>Relatório de <br>IA</h3>

            </div>

            <div class="card">

                <div class="card__icone">
                    <img src="${pageContext.request.contextPath}/assets/images/func-alertas.svg" alt="" width="56" height="56">
                </div>

                <h3>Alertas <br>em tempo real</h3>

            </div>

            <div class="card">

                <div class="card__icone">
                    <img src="${pageContext.request.contextPath}/assets/images/func-equipe.svg" alt="" width="62" height="62">
                </div>

                <h3>Integração da <br>equipe</h3>

            </div>

        </div>

    </div>

</section>


<h2 class="titulo-secao titulo-passos" id="passos">Passo a Passo</h2>

<section class="passos" aria-label="Passo a passo">

    <input class="controle" type="radio" name="passo" id="passo-1" checked>
    <input class="controle" type="radio" name="passo" id="passo-2">
    <input class="controle" type="radio" name="passo" id="passo-3">
    <input class="controle" type="radio" name="passo" id="passo-4">

    <div class="passos__trilho">

        <div class="passo passo--1" role="group" aria-label="Passo 1 de 4">

            <div class="passo__icone">
                <img src="${pageContext.request.contextPath}/assets/images/passo-1-mercado.svg" alt="" width="62" height="62">
            </div>

            <h3>Cadastre o mercado</h3>

            <p>
                Cadastre seu mercado, insira suas informações <br>
                e acesse sua dashboard principal
            </p>

        </div>

        <div class="passo passo--2" role="group" aria-label="Passo 2 de 4">

            <div class="passo__icone">
                <img src="${pageContext.request.contextPath}/assets/images/passo-2-dashboard.svg" alt="" width="66" height="66">
            </div>

            <h3>Acesse a dashboard</h3>

            <p>
                Após o cadastro, acesse a dashboard do <br>
                sistema, e personalize como quiser!
            </p>

        </div>

        <div class="passo passo--3" role="group" aria-label="Passo 3 de 4">

            <div class="passo__icone">
                <img src="${pageContext.request.contextPath}/assets/images/passo-3-mapa.svg" alt="" width="62" height="62">
            </div>

            <h3>Personalize o Mapa</h3>

            <p>
                Com sua dashboard aberta, acesse a area do mapa <br>
                e assim personalize com as escalações disponiveis
            </p>

        </div>

        <div class="passo passo--4" role="group" aria-label="Passo 4 de 4">

            <div class="passo__icone">
                <img src="${pageContext.request.contextPath}/assets/images/passo-4-relatorios.svg" alt="" width="66" height="66">
            </div>

            <h3>Receba os relatórios</h3>

            <p>
                Com tudo personalizado, receba seus relatórios mensais <br>
                de IA sobre o retrospecto do seu mercado
            </p>

        </div>

    </div>

    <div class="passos__indicador">

        <label class="passos__numero n1" for="passo-1" aria-label="Ir para o passo 1">1</label>

        <span class="passos__linha l1"></span>

        <label class="passos__numero n2" for="passo-2" aria-label="Ir para o passo 2">2</label>

        <span class="passos__linha l2"></span>

        <label class="passos__numero n3" for="passo-3" aria-label="Ir para o passo 3">3</label>

        <span class="passos__linha l3"></span>

        <label class="passos__numero n4" for="passo-4" aria-label="Ir para o passo 4">4</label>

    </div>

</section>


<section class="mapa" id="mapa">

    <div class="wrap mapa__conteudo">

        <svg class="mapa__curva" viewBox="0 0 1440 800" preserveAspectRatio="none" aria-hidden="true">

            <path d="M560 470 C440 330 520 120 700 115 C860 110 1000 150 1100 240"
                  fill="none"
                  stroke="#E8B333"
                  stroke-width="1.2"/>

        </svg>

        <span class="bolinha bolinha--amarela"></span>
        <span class="bolinha bolinha--verde"></span>

        <div class="mapa__texto">

            <h2>
                Mapa <span>completo</span> <br>
                e <span>interativo</span>
            </h2>

            <p class="mapa__descricao">
                Tenha uma visão clara das gondolas do seu mercado.
                Com informações atualizadas em tempo real!
            </p>

            <ul class="lista-check">

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Controle do estoque
                </li>

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Informações ao vivo
                </li>

                <li>
                    <img src="${pageContext.request.contextPath}/assets/images/check.svg" alt="" width="26" height="26">
                    Veja prateleiras vazias
                </li>

            </ul>

        </div>

        <div class="dispositivos">

            <div class="monitor">

                <img class="monitor__moldura"
                     src="${pageContext.request.contextPath}/assets/images/monitor.svg"
                     alt=""
                     width="700"
                     height="516">

                <div class="monitor__tela">

                    <img src="${pageContext.request.contextPath}/assets/images/tela-pc.png"
                         alt="Mapa da loja no painel do Kairos">

                </div>

            </div>

            <img class="celular"
                 src="${pageContext.request.contextPath}/assets/images/celular.png"
                 alt="Aplicativo Kairos no celular"
                 width="220"
                 height="454">

        </div>

    </div>

</section>


<section class="beneficios">

    <div class="wrap">

        <h2>Benefícios para o seu Mercado</h2>

        <div class="beneficios__faixa">

            <div class="beneficio">

                <img src="${pageContext.request.contextPath}/assets/images/beneficio-perdas.svg"
                     alt=""
                     width="46"
                     height="42">

                <span>
                    Reduza perdas <br>
                    e desperdicios
                </span>

            </div>

            <div class="beneficio">

                <img src="${pageContext.request.contextPath}/assets/images/beneficio-monitorar.svg"
                     alt=""
                     width="40"
                     height="40">

                <span>
                    Monitore seu <br>
                    Mercado
                </span>

            </div>

            <div class="beneficio">

                <img src="${pageContext.request.contextPath}/assets/images/beneficio-relatorios.svg"
                     alt=""
                     width="48"
                     height="40">

                <span>
                    Receba relatórios <br>
                    mensais de IA
                </span>

            </div>

            <div class="beneficio">

                <img src="${pageContext.request.contextPath}/assets/images/beneficio-cadastro.svg"
                     alt=""
                     width="40"
                     height="40">

                <span>
                    Cadastre seus produtos <br>
                    com facilidade
                </span>

            </div>

        </div>

    </div>

</section>


<section class="valores" id="valores">

    <svg class="deco deco--canto-direito" viewBox="0 0 200 240" aria-hidden="true">

        <path d="M32 0 C30 120 110 200 200 205 L200 0Z" fill="#E8B333"/>

        <path d="M8 0 C0 140 90 225 200 232" fill="none" stroke="#E8B333" stroke-width="1"/>

    </svg>

    <svg class="deco deco--canto-esquerdo" viewBox="0 0 250 170" aria-hidden="true">

        <path d="M0 30 C110 40 200 100 210 170 L0 170Z" fill="#E8B333"/>

        <path d="M0 0 C140 5 230 90 245 170" fill="none" stroke="#E8B333" stroke-width="1"/>

    </svg>

    <div class="wrap">

        <h2 class="titulo-secao titulo-valores">Nossos Valores</h2>

        <p class="valores__intro">
            São os princípios que guiam nossas decisões, nossa cultura <br>
            e nosso dia dia
        </p>

        <input class="controle" type="radio" name="valor" id="valor-1" checked>
        <input class="controle" type="radio" name="valor" id="valor-2">
        <input class="controle" type="radio" name="valor" id="valor-3">

        <div class="pilha" aria-label="Nossos valores">

            <div class="pilha__aba pilha__aba--fundo"></div>

            <div class="pilha__aba pilha__aba--meio"></div>

            <div class="valor valor--1">

                <div class="valor__texto">

                    <h3>Foco</h3>

                    <p>
                        Direcionar a mente e os esforços para um objetivo específico, eliminando distrações.
                    </p>

                </div>

                <img class="valor__icone"
                     src="${pageContext.request.contextPath}/assets/images/valor-foco.svg"
                     alt=""
                     width="180"
                     height="180">

            </div>

            <div class="valor valor--2">

                <div class="valor__texto">

                    <h3>Esperança</h3>

                    <p>
                        É a vontade e a crença de que uma situação difícil vai melhorar.
                    </p>

                </div>

                <img class="valor__icone"
                     src="${pageContext.request.contextPath}/assets/images/valor-esperanca.svg"
                     alt=""
                     width="176"
                     height="176">

            </div>

            <div class="valor valor--3">

                <div class="valor__texto">

                    <h3>Persistência</h3>

                    <p>
                        É a constância em buscar um resultado mesmo quando o caminho se torna difícil.
                    </p>

                </div>

                <img class="valor__icone"
                     src="${pageContext.request.contextPath}/assets/images/valor-persistencia.svg"
                     alt=""
                     width="180"
                     height="160">

            </div>

        </div>

        <div class="valores__botoes">

            <div class="setas setas--1">

                <label class="valores__anterior" for="valor-3" aria-label="Valor anterior">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-esquerda.svg" alt="" width="24" height="24">
                </label>

                <label class="valores__proximo" for="valor-2" aria-label="Próximo valor">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-direita.svg" alt="" width="24" height="24">
                </label>

            </div>

            <div class="setas setas--2">

                <label class="valores__anterior" for="valor-1" aria-label="Valor anterior">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-esquerda.svg" alt="" width="24" height="24">
                </label>

                <label class="valores__proximo" for="valor-3" aria-label="Próximo valor">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-direita.svg" alt="" width="24" height="24">
                </label>

            </div>

            <div class="setas setas--3">

                <label class="valores__anterior" for="valor-2" aria-label="Valor anterior">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-esquerda.svg" alt="" width="24" height="24">
                </label>

                <label class="valores__proximo" for="valor-1" aria-label="Próximo valor">
                    <img src="${pageContext.request.contextPath}/assets/images/seta-direita.svg" alt="" width="24" height="24">
                </label>

            </div>

        </div>

    </div>

</section>


<footer class="rodape">

    <div class="wrap rodape__colunas">

        <div class="rodape__coluna">

            <a href="#" class="logo logo--branco" aria-label="Kairos">

                <img src="${pageContext.request.contextPath}/assets/images/logo-kairos-branco.png"
                     alt=""
                     width="64"
                     height="92">

                <span>KAIROS</span>

            </a>

            <p>
                Gerencie produtos, <br>
                estoque e reposição <br>
                em <b>um só local</b>!
            </p>

            <div class="rodape__beneficios">

                <div>

                    <img src="${pageContext.request.contextPath}/assets/images/rodape-perdas.svg"
                         alt=""
                         width="38"
                         height="34">

                    Reduza perdas <br>
                    e desperdicios

                </div>

                <div>

                    <img src="${pageContext.request.contextPath}/assets/images/rodape-monitorar.svg"
                         alt=""
                         width="34"
                         height="34">

                    Monitore seu <br>
                    Mercado

                </div>

                <div>

                    <img src="${pageContext.request.contextPath}/assets/images/rodape-relatorios.svg"
                         alt=""
                         width="40"
                         height="32">

                    Receba relatórios <br>
                    mensais de IA

                </div>

            </div>

        </div>


        <div class="rodape__coluna">

            <h4>Nossas redes</h4>

            <p>
                <b>Conecte-se</b> com o Kairos em <br>
                todas as redes socias e se <br>
                <b>mantenha atualizado!</b>
            </p>

            <ul class="redes">

                <li>

                    <a href="https://instagram.com/kairos.app_" target="_blank" rel="noopener">

                        <img src="${pageContext.request.contextPath}/assets/images/instagram.svg"
                             alt=""
                             width="28"
                             height="28">

                        kairos.app_

                    </a>

                </li>

                <li>

                    <a href="https://wa.me/5511934891608" target="_blank" rel="noopener">

                        <img src="${pageContext.request.contextPath}/assets/images/whatsapp.svg"
                             alt=""
                             width="28"
                             height="28">

                        +55 11 93489-1608

                    </a>

                </li>

                <li>

                    <a href="mailto:kairos.intelligent@gmail.com">

                        <img src="${pageContext.request.contextPath}/assets/images/email.svg"
                             alt=""
                             width="28"
                             height="24">

                        kairos.intelligent@gmail.com

                    </a>

                </li>

            </ul>

        </div>


        <div class="rodape__coluna">

            <h4>Perguntas Frequentes</h4>

            <p>
                As principais <b>dúvidas</b> que nossos <br>
                usuarios tem ao utilizar o Kairos
            </p>

            <button class="botao-duvidas">Abrir dúvidas</button>

        </div>

    </div>

    <div class="rodape__copyright">
        Kairos&nbsp; 2026&nbsp; Todos os direitos reservados
    </div>

</footer>

</body>
</html>
