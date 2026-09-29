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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute("nome");
            session.removeAttribute("email");
            session.removeAttribute("senha");
            session.removeAttribute("dataNascimento");
            session.removeAttribute("cpf");
            session.removeAttribute("cep");
        }

        String destino = request.getParameter("destino");

        if ("index".equals(destino)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/login-page");
        }
    }
}
