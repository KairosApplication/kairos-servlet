package com.kairos.servlet;

import com.kairos.model.Setor;
import com.kairos.service.SetorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/setores")
public class SetorServlet extends HttpServlet {

    private final SetorService setorService = new SetorService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        // ==========================
        // DELETAR SETOR
        // ==========================

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idSetor = Integer.parseInt(id);

                setorService.deletarPorId(idSetor);
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/setores"
            );

            return;
        }

        // ==========================
        // PESQUISAR / LISTAR SETORES
        // ==========================

        String busca = request.getParameter("busca");

        List<Setor> setores;

        if (busca != null && !busca.isBlank()) {

            setores = setorService.pesquisar(busca);

        } else {

            setores = setorService.listarTodos();
        }

        request.setAttribute(
                "setores",
                setores
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/setores.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        String nome = request.getParameter("nome");

        String categoria = request.getParameter("categoria");

        // ==========================
        // ATUALIZAR SETOR
        // ==========================

        if (id != null) {

            int idSetor = Integer.parseInt(id);

            Setor setor = setorService.buscarPorId(idSetor);

            Map<String, String> erros = new HashMap<>();

            // ==========================
            // NOME
            // ==========================

            if (nome == null || nome.isBlank()) {

                erros.put(
                        "nome",
                        "Nome é obrigatório"
                );
            }

            // ==========================
            // CATEGORIA
            // ==========================

            if (categoria == null || categoria.isBlank()) {

                erros.put(
                        "categoria",
                        "Categoria é obrigatória"
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
                        "idSetorAtualizacao",
                        idSetor
                );

                List<Setor> setores =
                        setorService.listarTodos();

                request.setAttribute(
                        "setores",
                        setores
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/setores.jsp"
                ).forward(request, response);

                return;
            }

            // ==========================
            // ATUALIZAR
            // ==========================

            setor.setNome(nome);

            setor.setCategoria(categoria);

            setorService.atualizar(setor);

        } else {

            // ==========================
            // ADICIONAR SETOR
            // ==========================

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

            } else if (
                    setorService.existePorNome(nome)
            ) {

                erros.put(
                        "nome",
                        "Nome já cadastrado"
                );
            }

            // ==========================
            // CATEGORIA
            // ==========================

            if (categoria == null || categoria.isBlank()) {

                erros.put(
                        "categoria",
                        "Categoria é obrigatória"
                );
            }

            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "erros",
                        erros
                );

                List<Setor> setores =
                        setorService.listarTodos();

                request.setAttribute(
                        "setores",
                        setores
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/setores.jsp"
                ).forward(request, response);

                return;
            }

            // ==========================
            // CRIAR SETOR
            // ==========================

            Setor setor = new Setor(
                    nome,
                    categoria
            );

            setorService.cadastrar(setor);
        }

        // ==========================
        // REDIRECIONAR
        // ==========================

        response.sendRedirect(
                request.getContextPath() + "/admin/setores"
        );
    }
}