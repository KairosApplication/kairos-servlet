package com.kairos.servlet.auth;

import com.kairos.service.CadastroService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/cadastro-page")
public class CadastroPage1Servlet extends HttpServlet {

    private final CadastroService cadastroService;

    public CadastroPage1Servlet() {
        this.cadastroService = new CadastroService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            request.setAttribute("nome", session.getAttribute("nome"));
            request.setAttribute("email", session.getAttribute("email"));
            request.setAttribute("senha", session.getAttribute("senha"));
        }

        String sucesso = request.getParameter("sucesso");

        if ("true".equals(sucesso)) {
            request.setAttribute("sucesso", "Cadastro realizado com sucesso!");
        }

        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro1.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        request.setAttribute("nome", nome);
        request.setAttribute("email", email);
        request.setAttribute("senha", senha);

        List<String> erros = cadastroService.validarCadastro1(nome, email, senha);

        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);

            request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro1.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute("nome", nome);
        session.setAttribute("email", email);
        session.setAttribute("senha", senha);

        response.sendRedirect(request.getContextPath() + "/cadastro-page2");
    }
}