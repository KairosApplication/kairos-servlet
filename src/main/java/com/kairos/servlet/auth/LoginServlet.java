package com.kairos.servlet.auth;

import com.kairos.model.Admin;
import com.kairos.service.LoginService;
import com.kairos.utils.exceptions.system.AdminException;
import com.kairos.utils.exceptions.system.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Atributos
    private final LoginService loginService;

    // Construtor
    public LoginServlet() {
        this.loginService = new LoginService();
    }

    // Processa a autenticação do administrador
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Recupera os dados do formulário
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {

            // Autentica o administrador
            Admin admin = loginService.autenticar(email, senha);

            // Redireciona para a página de usuários
            response.sendRedirect(request.getContextPath() + "/admin/usuarios");

        } catch (AdminException e) {

            // Informa erro de autenticação
            request.setAttribute("erroEmailOuSenha", "E-mail ou senha inválidos");

            request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);

        } catch (ServiceException e) {

            // Informa erro do serviço
            request.setAttribute("erroService", e.getMessage());

            request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);
        }
    }
}
