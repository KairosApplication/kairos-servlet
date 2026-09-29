<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro - Kairos</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
    <script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>

<div class="page">
    <div class="circulo"></div>

    <a href="${pageContext.request.contextPath}/cadastro-sair?destino=index" class="logo">
        <img src="${pageContext.request.contextPath}/assets/images/logo-kairos.svg"
             alt="Logo Kairos">
    </a>

    <div class="container">
        <div class="left-col">
            <h1>Cadastre-se ou<br>faça Login!</h1>

            <div class="illustration">
                <img src="${pageContext.request.contextPath}/assets/images/ilustracao-login.svg"
                     alt="Ilustração de login">
            </div>
        </div>

        <div class="form-card">
            <div class="tabs">
                <button class="tab active" type="button">Cadastro</button>
                <a href="${pageContext.request.contextPath}/cadastro-sair?destino=login" class="tab login-tab">
                    Login
                </a>
            </div>

            <form action="${pageContext.request.contextPath}/cadastro-page2"
                  method="post">

                <div class="field">
                    <label for="datadenascimento">Data de nascimento</label>
                    <div class="input-wrap">
                        <input type="date"
                               id="datadenascimento"
                               name="dataNascimento"
                               value="${dataNascimento}"
                               placeholder="16/05/1986"
                               >
                    </div>

                    <%
                        if (request.getAttribute("erros") != null) {
                            for (String erro : (List<String>) request.getAttribute("erros")) {
                                if (erro.contains("Data de nascimento") || erro.contains("data de nascimento")) {
                    %>
                    <p class="erro"><%= erro %></p>
                    <%
                                }
                            }
                        }
                    %>

                </div>

                <div class="field">
                    <label for="cpf">CPF</label>
                    <div class="input-wrap">
                        <input type="text"
                               id="cpf"
                               name="cpf"
                               value="${cpf}"
                               placeholder="786.160.328-86"
                               >
                    </div>

                    <%
                        if (request.getAttribute("erros") != null) {
                            for (String erro : (List<String>) request.getAttribute("erros")) {
                                if (erro.contains("CPF")) {
                    %>
                    <p class="erro"><%= erro %></p>
                    <%
                                }
                            }
                        }
                    %>

                </div>

                <div class="field">
                    <label for="cep">CEP</label>
                    <div class="input-wrap">
                        <input type="text"
                               id="cep"
                               name="cep"
                               value="${cep}"
                               placeholder="16500-970"
                               >
                    </div>

                    <%
                        if (request.getAttribute("erros") != null) {
                            for (String erro : (List<String>) request.getAttribute("erros")) {
                                if (erro.contains("CEP")) {
                    %>
                    <p class="erro"><%= erro %></p>
                    <%
                                }
                            }
                        }
                    %>

                </div>

                <div class="tabs">
                    <a href="${pageContext.request.contextPath}/cadastro-page"
                       class="tab tab-extra">Voltar</a>
                    <button class="tab active tab-extra" type="submit">Criar Conta</button>
                </div>

            </form>

            <p class="terms">
                Ao continuar, você concorda com nossos<br>
                <a href="#">Termos de Uso</a>
            </p>
        </div>
    </div>
</div>

</body>
</html>