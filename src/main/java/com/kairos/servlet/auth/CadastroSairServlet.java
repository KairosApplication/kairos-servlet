package com.kairos.servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cadastro-sair")
public class CadastroSairServlet extends HttpServlet {

    // Processa a saída do cadastro
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Remove os dados do cadastro da sessão
        if (session != null) {
            session.removeAttribute("nome");
            session.removeAttribute("email");
            session.removeAttribute("senha");
            session.removeAttribute("dataNascimento");
            session.removeAttribute("cpf");
            session.removeAttribute("cep");
        }

        // Recupera o destino do redirecionamento
        String destino = request.getParameter("destino");

        // Redireciona para a página correspondente
        if ("index".equals(destino)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/login-page");
        }
    }
}
