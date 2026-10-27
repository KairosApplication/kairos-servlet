package com.kairos.servlet;

import com.kairos.model.Empresa;
import com.kairos.model.Gondola;
import com.kairos.model.Usuario;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.service.EmpresaService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.PasswordGenerator;
import com.kairos.utils.Regex;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final EmpresaService empresaService = new EmpresaService();

    // Processa a exclusão, pesquisa e listagem de usuários
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idUsuario = Integer.parseInt(id);

                usuarioService.deletarPorId(idUsuario);
            }

            response.sendRedirect(request.getContextPath() + "/admin/usuarios");

            return;
        }

        String busca = request.getParameter("busca");

        List<Usuario> usuarios;

        if (busca != null && !busca.isBlank()) {
            usuarios = usuarioService.pesquisar(busca.trim());
        } else {
            usuarios = usuarioService.listarTodos();
        }

        // Recupera as gôndolas vinculadas a cada usuário
        Map<Integer, List<Gondola>> gondolasPorUsuario = new HashMap<>();

        for (Usuario usuario : usuarios) {

            List<Gondola> gondolas = usuarioService.listarGondolasPorUsuario(usuario);

            gondolasPorUsuario.put(usuario.getId(), gondolas);
        }

        // Recupera possíveis erros de vinculação armazenados na sessão
        String erroVincularGondola = (String) request.getSession().getAttribute("erroVincularGondola");
        Integer idUsuarioVincular = (Integer) request.getSession().getAttribute("idUsuarioVincular");
        Integer idGondolaVincular = (Integer) request.getSession().getAttribute("idGondolaVincular");

        if (erroVincularGondola != null) {

            request.setAttribute("erroVincularGondola", erroVincularGondola);
            request.setAttribute("idUsuarioVincular", idUsuarioVincular);
            request.setAttribute("idGondolaVincular", idGondolaVincular);

            request.getSession().removeAttribute("erroVincularGondola");
            request.getSession().removeAttribute("idUsuarioVincular");
            request.getSession().removeAttribute("idGondolaVincular");
        }

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("gondolasPorUsuario", gondolasPorUsuario);

        request.getRequestDispatcher("/WEB-INF/views/admin/usuarios.jsp")
                .forward(request, response);
    }

    // Processa o cadastro ou a atualização de usuários
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        String nome = request.getParameter("nome");
        String sobrenome = request.getParameter("sobrenome");
        String cpf = request.getParameter("cpf");
        String dataNascimento = request.getParameter("nascimento");
        String tipoUsuario = request.getParameter("tipoUsuario");
        String cep = request.getParameter("cep");
        String email = request.getParameter("email");

        if (id != null) {

            // Atualiza os dados de um usuário existente
            int idUsuario = Integer.parseInt(id);

            Usuario usuario = usuarioService.buscarPorId(idUsuario);

            Map<String, String> erros = new HashMap<>();

            // Valida os campos permitidos na atualização
            if (nome == null || nome.isBlank()) {

                erros.put("nome", "Nome é obrigatório");

            } else if (!nome.matches("[\\p{L} ]+")) {
                erros.put("nome", "Nome deve conter apenas letras");
            }

            if (sobrenome == null || sobrenome.isBlank()) {

                erros.put("sobrenome", "Sobrenome é obrigatório");

            } else if (!sobrenome.matches("[\\p{L} ]+")) {
                erros.put("sobrenome", "Sobrenome deve conter apenas letras");
            }

            if (cep == null || cep.isBlank()) {

                erros.put("cep", "CEP é obrigatório");

            } else if (!cep.matches(Regex.CEP)) {
                erros.put("cep", "Formato do CEP inválido");
            }

            if (email == null || email.isBlank()) {

                erros.put("email", "Email é obrigatório");

            } else if (!email.matches(Regex.EMAIL)) {
                erros.put("email", "Formato do email inválido");
            } else if (usuarioService.existePorEmailExcetoId(email, idUsuario)) {
                erros.put("email", "Email já cadastrado");
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("errosAtualizacao", erros);
                request.setAttribute("idUsuarioAtualizacao", idUsuario);

                List<Usuario> usuarios = usuarioService.listarTodos();
                Map<Integer, List<Gondola>> gondolasPorUsuario = new HashMap<>();

                for (Usuario usuarioLista : usuarios) {

                    List<Gondola> gondolas = usuarioService.listarGondolasPorUsuario(usuarioLista);

                    gondolasPorUsuario.put(usuarioLista.getId(), gondolas);
                }

                request.setAttribute("usuarios", usuarios);
                request.setAttribute("gondolasPorUsuario", gondolasPorUsuario);

                request.getRequestDispatcher("/WEB-INF/views/admin/usuarios.jsp")
                        .forward(request, response);

                return;
            }

            // Mantém o CPF e a data de nascimento originais do usuário
            usuario.setNome(nome);
            usuario.setSobrenome(sobrenome);
            usuario.setCep(cep);
            usuario.setEmail(email);

            usuarioService.atualizar(usuario);

        } else {

            // Cadastra um novo usuário
            String nomeEmpresa = request.getParameter("nomeEmpresa");

            Map<String, String> erros = new HashMap<>();

            LocalDate dataNascimentoConvertida = null;

            // Valida os campos obrigatórios do cadastro
            if (nome == null || nome.isBlank()) {

                erros.put("nome", "Nome é obrigatório");

            } else if (!nome.matches("[\\p{L} ]+")) {
                erros.put("nome", "Nome deve conter apenas letras");
            }

            if (sobrenome == null || sobrenome.isBlank()) {
                erros.put("sobrenome", "Sobrenome é obrigatório");
            } else if (!sobrenome.matches("[\\p{L} ]+")) {
                erros.put("sobrenome", "Sobrenome deve conter apenas letras");
            }

            if (cpf == null || cpf.isBlank()) {

                erros.put("cpf", "CPF é obrigatório");

            } else if (!cpf.matches(Regex.CPF)) {
                erros.put("cpf", "Formato do CPF inválido");
            } else if (usuarioService.existePorCpf(cpf)) {
                erros.put("cpf", "CPF já cadastrado");
            }

            // Converte e valida a data de nascimento
            if (dataNascimento == null || dataNascimento.isBlank()) {

                erros.put("nascimento", "Data de nascimento é obrigatória");

            } else {

                try {

                    dataNascimentoConvertida = LocalDate.parse(dataNascimento);

                    if (dataNascimentoConvertida.isBefore(LocalDate.of(1900, 1, 1))) {

                        erros.put("nascimento", "Data de nascimento deve ser a partir de 1900");

                    } else if (dataNascimentoConvertida.isAfter(LocalDate.now())) {

                        erros.put("nascimento", "Data de nascimento não pode ser futura");
                    }

                } catch (DateTimeParseException e) {
                    erros.put("nascimento", "Data de nascimento inválida");
                }
            }

            if (cep == null || cep.isBlank()) {

                erros.put("cep", "CEP é obrigatório");

            } else if (!cep.matches(Regex.CEP)) {
                erros.put("cep", "Formato do CEP inválido");
            }

            if (email == null || email.isBlank()) {

                erros.put("email", "Email é obrigatório");

            } else if (!email.matches(Regex.EMAIL)) {
                erros.put("email", "Formato do email inválido");
            } else if (usuarioService.existePorEmail(email)) {
                erros.put("email", "Email já cadastrado");
            }

            if (tipoUsuario == null || tipoUsuario.isBlank()) {
                erros.put("tipoUsuario", "Tipo de usuário é obrigatório");
            }

            // Busca e valida a empresa informada
            Empresa empresa = null;

            if (nomeEmpresa == null || nomeEmpresa.isBlank()) {

                erros.put("empresa", "Nome da empresa é obrigatório");

            } else {

                try {

                    empresa = empresaService.buscarPorNome(nomeEmpresa);

                } catch (EmpresaNotFoundException e) {
                    erros.put("empresa", "Empresa não encontrada");
                }
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("erros", erros);

                List<Usuario> usuarios = usuarioService.listarTodos();
                Map<Integer, List<Gondola>> gondolasPorUsuario = new HashMap<>();

                for (Usuario usuario : usuarios) {

                    List<Gondola> gondolas = usuarioService.listarGondolasPorUsuario(usuario);

                    gondolasPorUsuario.put(usuario.getId(), gondolas);
                }

                request.setAttribute("usuarios", usuarios);
                request.setAttribute("gondolasPorUsuario", gondolasPorUsuario);

                request.getRequestDispatcher("/WEB-INF/views/admin/usuarios.jsp")
                        .forward(request, response);

                return;
            }

            // Gera uma senha e cadastra o usuário validado
            String senha = PasswordGenerator.gerar(15);

            Usuario usuario = new Usuario(
                    cpf,
                    senha,
                    nome,
                    sobrenome,
                    dataNascimentoConvertida,
                    cep,
                    TipoUsuario.valueOf(tipoUsuario),
                    email,
                    empresa
            );

            usuarioService.cadastrar(usuario);
        }

        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }
}
