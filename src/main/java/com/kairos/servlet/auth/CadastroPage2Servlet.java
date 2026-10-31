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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cadastro-page2")
public class CadastroPage2Servlet extends HttpServlet {

    // Atributos
    private final CadastroService cadastroService;

    // Construtor
    public CadastroPage2Servlet() {
        this.cadastroService = new CadastroService();
    }

    // Carrega a segunda página de cadastro
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Recupera os dados da sessão
        if (session != null) {
            request.setAttribute("dataNascimento", session.getAttribute("dataNascimento"));
            request.setAttribute("cpf", session.getAttribute("cpf"));
            request.setAttribute("cep", session.getAttribute("cep"));
        }

        // Encaminha para a página de cadastro
        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro2.jsp").forward(request, response);
    }

    // Processa a segunda etapa do cadastro
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Recupera os dados do formulário
        String dataNascimentoParam = request.getParameter("dataNascimento");
        String cpf = request.getParameter("cpf");
        String cep = request.getParameter("cep");

        request.setAttribute("dataNascimento", dataNascimentoParam);
        request.setAttribute("cpf", cpf);
        request.setAttribute("cep", cep);

        // Converte a data de nascimento
        LocalDate dataNascimento = null;
        List<String> erros = new ArrayList<>();
        boolean dataInvalida = false;

        try {
            if (dataNascimentoParam != null && !dataNascimentoParam.isBlank()) {
                dataNascimento = LocalDate.parse(dataNascimentoParam);
            }
        } catch (DateTimeParseException e) {
            dataInvalida = true;
            erros.add("Data de nascimento inválida");
        }

        // Valida os dados informados
        List<String> errosValidacao = new ArrayList<>(
                cadastroService.validarCadastro2(dataNascimento, cpf, cep)
        );

        // Evita mensagens duplicadas para a data
        if (dataInvalida) {
            errosValidacao.removeIf(erro ->
                    erro.toLowerCase().contains("data de nascimento")
            );
        }

        erros.addAll(errosValidacao);

        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);

            request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro2.jsp").forward(request, response);
            return;
        }

        // Limpa os dados da sessão
        HttpSession session = request.getSession();

        session.removeAttribute("nome");
        session.removeAttribute("email");
        session.removeAttribute("senha");
        session.removeAttribute("dataNascimento");
        session.removeAttribute("cpf");
        session.removeAttribute("cep");

        // Redireciona para a primeira página com mensagem de sucesso
        response.sendRedirect(request.getContextPath() + "/cadastro-page?sucesso=true");
    }
}
