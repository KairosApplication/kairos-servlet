<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Kairos</title>

    <link rel="stylesheet" href="assets/">
    <script src="" defer></script>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
</head>

<body>
<div class="page">
    <div class="circulo"></div>

    <!-- logo -->
    <a href="../../../index.jsp" class="logo">
        <img src="../../../assets/images" alt="Logo Kairos">
    </a>

    <div class="container">
        <div class="left-col">
            <h1>
                Cadastre-se ou <br> faça Login!
            </h1>
            <div class="illustration">
                <img src="../../../assets/images" alt="Ilustração de login">
            </div>
        </div>

        <!-- Card do formulário -->
        <div class="form-card">
            <div class="tabs">
                <a href="../cadastro/cadastro.jsp" class="tab">Cadastro</a>
                <button class="tab active" type="button">Login</button>
            </div>

            <!-- Formulário de Login -->
            <form action="login.jsp" method="post">
                <div class="field">
                    <label for="email">E-mail</label>
                    <div class="input-wrap">
                        <input type="email" id="email" name="email"
                               placeholder="paulo@gmail.com.br" required>
                    </div>
                </div>
                <div class="field">
                    <label for="senha">Senha</label>
                    <div class="input-wrap">
                        <input type="password" id="senha" name="senha"
                               placeholder="Senha"
                               required>
                        <span class="toggle-eye" onclick="toggleSenha()">
                            <svg id="eyeIcon" width="20" height="20" viewBox="0 0 24 24"
                                 fill="none" stroke="currentColor" stroke-width="2"
                                 stroke-linecap="round" stroke-linejoin="round">

                                <path d="M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a18.5 18.5 0 0 1 5.06-5.94"></path>
                                <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"></path>

                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </svg>
                        </span>
                    </div>
                </div>
                <button class="submit-btn" type="submit">Prosseguir</button>
            </form>

            <p class="terms">Ao continuar, você concorda com nossos<br><a href="#">Termos de Uso</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
