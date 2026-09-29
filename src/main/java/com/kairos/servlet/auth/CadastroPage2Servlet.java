package com.kairos.servlet.auth;

import com.kairos.service.CadastroService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/cadastro-page2")
public class CadastroPage2Servlet extends HttpServlet {

    private final CadastroService cadastroService;

    public CadastroPage2Servlet() {
        this.cadastroService = new CadastroService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            request.setAttribute("dataNascimento", session.getAttribute("dataNascimento"));
            request.setAttribute("cpf", session.getAttribute("cpf"));
            request.setAttribute("cep", session.getAttribute("cep"));
        }

        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro2.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dataNascimentoParam = request.getParameter("dataNascimento");
        String cpf = request.getParameter("cpf");
        String cep = request.getParameter("cep");

        request.setAttribute("dataNascimento", dataNascimentoParam);
        request.setAttribute("cpf", cpf);
        request.setAttribute("cep", cep);

        LocalDate dataNascimento = null;

        if (dataNascimentoParam != null && !dataNascimentoParam.isBlank()) {
            dataNascimento = LocalDate.parse(dataNascimentoParam);
        }

        List<String> erros = cadastroService.validarCadastro2(
                dataNascimento,
                cpf,
                cep
        );

        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);

            request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro2.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        session.removeAttribute("nome");
        session.removeAttribute("email");
        session.removeAttribute("senha");
        session.removeAttribute("dataNascimento");
        session.removeAttribute("cpf");
        session.removeAttribute("cep");

        response.sendRedirect(
                request.getContextPath() + "/cadastro-page?sucesso=true"
        );
    }
}