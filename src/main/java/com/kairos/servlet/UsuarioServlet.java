package com.kairos.servlet;

import com.kairos.model.Gondola;
import com.kairos.model.Empresa;
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
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final EmpresaService empresaService = new EmpresaService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idUsuario = Integer.parseInt(id);

                usuarioService.deletarPorId(idUsuario);
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/usuarios"
            );

            return;
        }

        String busca = request.getParameter("busca");

        List<Usuario> usuarios;

        if (busca != null && !busca.isBlank()) {
            usuarios = usuarioService.pesquisar(busca);
        } else {
            usuarios = usuarioService.listarTodos();
        }

        Map<Integer, List<Gondola>> gondolasPorUsuario =
                new HashMap<>();

        for (Usuario usuario : usuarios) {

            List<Gondola> gondolas =
                    usuarioService.listarGondolasPorUsuario(usuario);

            gondolasPorUsuario.put(
                    usuario.getId(),
                    gondolas
            );
        }

        String erroVincularGondola =
                (String) request.getSession()
                        .getAttribute("erroVincularGondola");

        Integer idUsuarioVincular =
                (Integer) request.getSession()
                        .getAttribute("idUsuarioVincular");

        Integer idGondolaVincular =
                (Integer) request.getSession()
                        .getAttribute("idGondolaVincular");

        if (erroVincularGondola != null) {

            request.setAttribute(
                    "erroVincularGondola",
                    erroVincularGondola
            );

            request.setAttribute(
                    "idUsuarioVincular",
                    idUsuarioVincular
            );

            request.setAttribute(
                    "idGondolaVincular",
                    idGondolaVincular
            );

            request.getSession()
                    .removeAttribute("erroVincularGondola");

            request.getSession()
                    .removeAttribute("idUsuarioVincular");

            request.getSession()
                    .removeAttribute("idGondolaVincular");
        }

        request.setAttribute(
                "usuarios",
                usuarios
        );

        request.setAttribute(
                "gondolasPorUsuario",
                gondolasPorUsuario
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/usuarios.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        String nome = request.getParameter("nome");
        String sobrenome = request.getParameter("sobrenome");
        String cpf = request.getParameter("cpf");
        String dataNascimento = request.getParameter("nascimento");
        String tipoUsuario = request.getParameter("tipoUsuario");
        String cep = request.getParameter("cep");
        String email = request.getParameter("email");

        if (id != null) {


            if (id != null) {

                // ==========================
                // ATUALIZAR USUÁRIO
                // ==========================

                int idUsuario = Integer.parseInt(id);

                Usuario usuario =
                        usuarioService.buscarPorId(idUsuario);

                Map<String, String> erros = new HashMap<>();

                // ==========================
                // NOME
                // ==========================

                if (nome == null || nome.isBlank()) {

                    erros.put(
                            "nome",
                            "Nome é obrigatório"
                    );

                } else if (!nome.matches("[\\p{L} ]+")) {

                    erros.put(
                            "nome",
                            "Nome deve conter apenas letras"
                    );
                }

                // ==========================
                // SOBRENOME
                // ==========================

                if (sobrenome == null || sobrenome.isBlank()) {

                    erros.put(
                            "sobrenome",
                            "Sobrenome é obrigatório"
                    );

                } else if (!sobrenome.matches("[\\p{L} ]+")) {

                    erros.put(
                            "sobrenome",
                            "Sobrenome deve conter apenas letras"
                    );
                }

                // ==========================
                // CPF
                // ==========================

                if (cpf == null || cpf.isBlank()) {

                    erros.put(
                            "cpf",
                            "CPF é obrigatório"
                    );

                } else if (!cpf.matches(Regex.CPF)) {

                    erros.put(
                            "cpf",
                            "Formato do CPF inválido"
                    );

                } else if (usuarioService.existePorCpfExcetoId(cpf, idUsuario)) {

                    erros.put(
                            "cpf",
                            "CPF já cadastrado"
                    );
                }

                // ==========================
                // DATA DE NASCIMENTO
                // ==========================

                LocalDate data = null;

                if (dataNascimento == null || dataNascimento.isBlank()) {

                    erros.put(
                            "nascimento",
                            "Data de nascimento é obrigatória"
                    );

                } else {

                    data = LocalDate.parse(dataNascimento);

                    if (data.isBefore(
                            LocalDate.of(1900, 1, 1)
                    )) {

                        erros.put(
                                "nascimento",
                                "Data de nascimento deve ser a partir de 1900"
                        );

                    } else if (data.isAfter(LocalDate.now())) {

                        erros.put(
                                "nascimento",
                                "Data de nascimento não pode ser futura"
                        );
                    }
                }

                // ==========================
                // CEP
                // ==========================

                if (cep == null || cep.isBlank()) {

                    erros.put(
                            "cep",
                            "CEP é obrigatório"
                    );

                } else if (!cep.matches(Regex.CEP)) {

                    erros.put(
                            "cep",
                            "Formato do CEP inválido"
                    );
                }

                // ==========================
                // EMAIL
                // ==========================

                if (email == null || email.isBlank()) {

                    erros.put(
                            "email",
                            "Email é obrigatório"
                    );

                } else if (!email.matches(Regex.EMAIL)) {

                    erros.put(
                            "email",
                            "Formato do email inválido"
                    );

                } else if (usuarioService.existePorEmailExcetoId(email, idUsuario)) {

                    erros.put(
                            "email",
                            "Email já cadastrado"
                    );
                }

                // ==========================
                // EXISTEM ERROS
                // ==========================

                if (!erros.isEmpty()) {

                    request.setAttribute(
                            "errosAtualizacao",
                            erros
                    );

                    request.setAttribute(
                            "idUsuarioAtualizacao",
                            idUsuario
                    );

                    List<Usuario> usuarios =
                            usuarioService.listarTodos();

                    Map<Integer, List<Gondola>> gondolasPorUsuario =
                            new HashMap<>();

                    for (Usuario usuarioLista : usuarios) {

                        List<Gondola> gondolas =
                                usuarioService.listarGondolasPorUsuario(
                                        usuarioLista
                                );

                        gondolasPorUsuario.put(
                                usuarioLista.getId(),
                                gondolas
                        );
                    }

                    request.setAttribute(
                            "usuarios",
                            usuarios
                    );

                    request.setAttribute(
                            "gondolasPorUsuario",
                            gondolasPorUsuario
                    );

                    request.getRequestDispatcher(
                            "/WEB-INF/views/admin/usuarios.jsp"
                    ).forward(request, response);

                    return;
                }

                // ==========================
                // ATUALIZAR
                // ==========================

                usuario.setCpf(cpf);
                usuario.setNome(nome);
                usuario.setSobrenome(sobrenome);
                usuario.setDataNascimento(data);
                usuario.setCep(cep);
                usuario.setEmail(email);

                usuarioService.atualizar(usuario);
            }

        } else {

            // ==========================
            // ADICIONAR USUÁRIO
            // ==========================

            String nomeEmpresa =
                    request.getParameter("nomeEmpresa");

            Map<String, String> erros =
                    new HashMap<>();

            // ==========================
            // NOME
            // ==========================

            if (nome == null || nome.isBlank()) {

                erros.put(
                        "nome",
                        "Nome é obrigatório"
                );
            } else if (!nome.matches("[\\p{L} ]+")) {
                erros.put(
                        "nome",
                        "Nome deve conter apenas letras"
                );
            }

            // ==========================
            // SOBRENOME
            // ==========================

            if (sobrenome == null || sobrenome.isBlank()) {

                erros.put(
                        "sobrenome",
                        "Sobrenome é obrigatório"
                );
            } else if (!sobrenome.matches("[\\p{L} ]+")) {
                erros.put(
                        "sobrenome",
                        "Sobrenome deve conter apenas letras"
                );
            }

            // ==========================
            // CPF
            // ==========================

            if (cpf == null || cpf.isBlank()) {

                erros.put(
                        "cpf",
                        "CPF é obrigatório"
                );

            } else if (!cpf.matches(Regex.CPF)) {

                erros.put(
                        "cpf",
                        "Formato do CPF inválido"
                );
            } else if (usuarioService.existePorCpf(cpf)) {
                erros.put(
                        "cpf",
                        "CPF já cadastrado"
                );
            }

            // ==========================
            // DATA DE NASCIMENTO
            // ==========================

            if (dataNascimento == null || dataNascimento.isBlank()) {

                erros.put(
                        "nascimento",
                        "Data de nascimento é obrigatória"
                );

            } else {

                LocalDate data =
                        LocalDate.parse(dataNascimento);

                if (data.isBefore(
                        LocalDate.of(1900, 1, 1)
                )) {

                    erros.put(
                            "nascimento",
                            "Data de nascimento deve ser a partir de 1900"
                    );

                } else if (data.isAfter(LocalDate.now())) {

                    erros.put(
                            "nascimento",
                            "Data de nascimento não pode ser futura"
                    );
                }
            }

            // ==========================
            // CEP
            // ==========================

            if (cep == null || cep.isBlank()) {

                erros.put(
                        "cep",
                        "CEP é obrigatório"
                );

            } else if (!cep.matches(Regex.CEP)) {

                erros.put(
                        "cep",
                        "Formato do CEP inválido"
                );
            }

            // ==========================
            // EMAIL
            // ==========================

            if (email == null || email.isBlank()) {

                erros.put(
                        "email",
                        "Email é obrigatório"
                );

            } else if (!email.matches(Regex.EMAIL)) {

                erros.put(
                        "email",
                        "Formato do email inválido"
                );
            } else if (usuarioService.existePorEmail(email)) {
                erros.put(
                        "email",
                        "Email já cadastrado"
                );
            }

            // ==========================
            // TIPO USUÁRIO
            // ==========================

            if (tipoUsuario == null || tipoUsuario.isBlank()) {

                erros.put(
                        "tipoUsuario",
                        "Tipo de usuário é obrigatório"
                );
            }

            // ==========================
            // EMPRESA
            // ==========================

            Empresa empresa = null;

            if (nomeEmpresa == null || nomeEmpresa.isBlank()) {

                erros.put(
                        "empresa",
                        "Nome da empresa é obrigatório"
                );

            } else {

                try {

                    empresa =
                            empresaService.buscarPorNome(nomeEmpresa);

                } catch (EmpresaNotFoundException e) {

                    erros.put(
                            "empresa",
                            "Empresa não encontrada"
                    );
                }
            }

            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "erros",
                        erros
                );

                List<Usuario> usuarios =
                        usuarioService.listarTodos();

                Map<Integer, List<Gondola>> gondolasPorUsuario =
                        new HashMap<>();

                for (Usuario usuario : usuarios) {

                    List<Gondola> gondolas =
                            usuarioService
                                    .listarGondolasPorUsuario(usuario);

                    gondolasPorUsuario.put(
                            usuario.getId(),
                            gondolas
                    );
                }

                request.setAttribute(
                        "usuarios",
                        usuarios
                );

                request.setAttribute(
                        "gondolasPorUsuario",
                        gondolasPorUsuario
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/usuarios.jsp"
                ).forward(request, response);

                return;
            }

            // ==========================
            // CRIAR USUÁRIO
            // ==========================

            String senha =
                    PasswordGenerator.gerar(15);

            Usuario usuario = new Usuario(
                    cpf,
                    senha,
                    nome,
                    sobrenome,
                    LocalDate.parse(dataNascimento),
                    cep,
                    TipoUsuario.valueOf(tipoUsuario),
                    email,
                    empresa
            );

            usuarioService.cadastrar(usuario);
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/usuarios"
        );
    }
}