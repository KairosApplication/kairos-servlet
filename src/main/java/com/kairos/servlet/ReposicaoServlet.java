package com.kairos.servlet;

import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.model.Reposicao;
import com.kairos.model.Usuario;
import com.kairos.service.GondolaService;
import com.kairos.service.ProdutoService;
import com.kairos.service.ReposicaoService;
import com.kairos.service.UsuarioReposicaoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/reposicoes")
public class ReposicaoServlet extends HttpServlet {

    private final ReposicaoService reposicaoService =
            new ReposicaoService();

    private final ProdutoService produtoService =
            new ProdutoService();

    private final GondolaService gondolaService =
            new GondolaService();

    private final UsuarioReposicaoService usuarioReposicaoService =
            new UsuarioReposicaoService();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao =
                request.getParameter("acao");


        // ==========================
        // DELETAR REPOSIÇÃO
        // ==========================

        if ("deletar".equals(acao)) {

            String id =
                    request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idReposicao =
                        Integer.parseInt(id);

                reposicaoService.deletarPorId(
                        idReposicao
                );
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/reposicoes"
            );

            return;
        }


        // ==========================
        // PESQUISAR / LISTAR REPOSIÇÕES
        // ==========================

        String busca =
                request.getParameter("busca");

        List<Reposicao> reposicoes;

        if (busca != null && !busca.isBlank()) {

            reposicoes =
                    reposicaoService.pesquisar(
                            busca.trim()
                    );

        } else {

            reposicoes =
                    reposicaoService.listarTodos();
        }


        // ==========================
        // USUÁRIOS POR REPOSIÇÃO
        // ==========================

        Map<Integer, List<Usuario>> usuariosPorReposicao =
                new HashMap<>();

        for (Reposicao reposicao : reposicoes) {

            List<Usuario> usuarios =
                    usuarioReposicaoService
                            .listarUsuariosPorReposicao(
                                    reposicao
                            );

            usuariosPorReposicao.put(
                    reposicao.getId(),
                    usuarios
            );
        }


        // ==========================
        // PRODUTOS E GÔNDOLAS
        // ==========================

        List<Produto> produtos =
                produtoService.listarTodos();

        List<Gondola> gondolas =
                gondolaService.listarTodos();


        // ==========================
        // ATRIBUTOS
        // ==========================

        request.setAttribute(
                "reposicoes",
                reposicoes
        );

        request.setAttribute(
                "produtos",
                produtos
        );

        request.setAttribute(
                "gondolas",
                gondolas
        );

        request.setAttribute(
                "usuariosPorReposicao",
                usuariosPorReposicao
        );


        // ==========================
        // ERRO AO VINCULAR FUNCIONÁRIO
        // ==========================

        HttpSession session =
                request.getSession();

        String erroVincularReposicao =
                (String) session.getAttribute(
                        "erroVincularReposicao"
                );

        Object idUsuarioVincular =
                session.getAttribute(
                        "idUsuarioVincular"
                );

        Object idReposicaoVincular =
                session.getAttribute(
                        "idReposicaoVincular"
                );

        request.setAttribute(
                "erroVincularReposicao",
                erroVincularReposicao
        );

        request.setAttribute(
                "idUsuarioVincular",
                idUsuarioVincular
        );

        request.setAttribute(
                "idReposicaoVincular",
                idReposicaoVincular
        );

        session.removeAttribute(
                "erroVincularReposicao"
        );

        session.removeAttribute(
                "idUsuarioVincular"
        );

        session.removeAttribute(
                "idReposicaoVincular"
        );


        // ==========================
        // FORWARD
        // ==========================

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/reposicoes.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String id =
                request.getParameter("id");

        String quantidadeReposto =
                request.getParameter("quantidadeReposto");

        String motivoReposicao =
                request.getParameter("motivoReposicao");


        // ==========================
        // ATUALIZAR REPOSIÇÃO
        // ==========================

        if (id != null) {

            int idReposicao =
                    Integer.parseInt(id);

            Reposicao reposicao =
                    reposicaoService.buscarPorId(
                            idReposicao
                    );

            Map<String, String> erros =
                    new HashMap<>();

            int quantidade =
                    Integer.parseInt(
                            quantidadeReposto
                    );


            // ==========================
            // MOTIVO
            // ==========================

            if (
                    motivoReposicao == null
                            || motivoReposicao.isBlank()
            ) {

                erros.put(
                        "motivoReposicao",
                        "Motivo da reposição é obrigatório"
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
                        "idReposicaoAtualizacao",
                        idReposicao
                );


                List<Reposicao> reposicoes =
                        reposicaoService.listarTodos();

                Map<Integer, List<Usuario>> usuariosPorReposicao =
                        new HashMap<>();

                for (Reposicao reposicaoLista : reposicoes) {

                    List<Usuario> usuarios =
                            usuarioReposicaoService
                                    .listarUsuariosPorReposicao(
                                            reposicaoLista
                                    );

                    usuariosPorReposicao.put(
                            reposicaoLista.getId(),
                            usuarios
                    );
                }


                List<Produto> produtos =
                        produtoService.listarTodos();

                List<Gondola> gondolas =
                        gondolaService.listarTodos();


                request.setAttribute(
                        "reposicoes",
                        reposicoes
                );

                request.setAttribute(
                        "produtos",
                        produtos
                );

                request.setAttribute(
                        "gondolas",
                        gondolas
                );

                request.setAttribute(
                        "usuariosPorReposicao",
                        usuariosPorReposicao
                );


                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/reposicoes.jsp"
                ).forward(request, response);

                return;
            }


            // ==========================
            // ATUALIZAR SOMENTE
            // QUANTIDADE E MOTIVO
            // ==========================

            reposicao.setQuantidadeReposto(
                    quantidade
            );

            reposicao.setMotivoReposicao(
                    motivoReposicao
            );

            reposicaoService.atualizar(
                    reposicao
            );


        } else {

            String produtoId =
                    request.getParameter("produtoId");

            String dataReposicao =
                    request.getParameter("dataReposicao");

            String gondolaId =
                    request.getParameter("gondolaId");


            // ==========================
            // ADICIONAR REPOSIÇÃO
            // ==========================

            Map<String, String> erros =
                    new HashMap<>();

            Produto produto = null;
            Gondola gondola = null;
            Timestamp data = null;

            int quantidade = 0;


            // ==========================
            // PRODUTO
            // ==========================

            if (
                    produtoId == null
                            || produtoId.isBlank()
            ) {

                erros.put(
                        "produtoId",
                        "Produto é obrigatório"
                );

            } else {

                try {

                    int idProduto =
                            Integer.parseInt(
                                    produtoId
                            );

                    produto =
                            produtoService.buscarPorId(
                                    idProduto
                            );

                } catch (NumberFormatException e) {

                    erros.put(
                            "produtoId",
                            "Produto deve ser um número válido"
                    );
                }
            }


            // ==========================
            // DATA
            // ==========================

            if (
                    dataReposicao == null
                            || dataReposicao.isBlank()
            ) {

                erros.put(
                        "dataReposicao",
                        "Data da reposição é obrigatória"
                );

            } else {

                try {

                    data = Timestamp.valueOf(
                            dataReposicao.replace("T", " ") + ":00"
                    );

                } catch (IllegalArgumentException e) {

                    erros.put(
                            "dataReposicao",
                            "Data da reposição inválida"
                    );
                }
            }


            // ==========================
            // QUANTIDADE
            // ==========================

            if (
                    quantidadeReposto == null
                            || quantidadeReposto.isBlank()
            ) {

                erros.put(
                        "quantidadeReposto",
                        "Quantidade reposta é obrigatória"
                );

            } else {

                try {

                    quantidade =
                            Integer.parseInt(
                                    quantidadeReposto
                            );

                } catch (NumberFormatException e) {

                    erros.put(
                            "quantidadeReposto",
                            "Quantidade reposta deve ser um número válido"
                    );
                }
            }


            // ==========================
            // MOTIVO
            // ==========================

            if (
                    motivoReposicao == null
                            || motivoReposicao.isBlank()
            ) {

                erros.put(
                        "motivoReposicao",
                        "Motivo da reposição é obrigatório"
                );
            }


            // ==========================
            // GÔNDOLA
            // ==========================

            if (
                    gondolaId == null
                            || gondolaId.isBlank()
            ) {

                erros.put(
                        "gondolaId",
                        "Gôndola é obrigatória"
                );

            } else {

                try {

                    int idGondola =
                            Integer.parseInt(
                                    gondolaId
                            );

                    gondola =
                            gondolaService.buscarPorId(
                                    idGondola
                            );

                } catch (NumberFormatException e) {

                    erros.put(
                            "gondolaId",
                            "Gôndola deve ser um número válido"
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


                List<Reposicao> reposicoes =
                        reposicaoService.listarTodos();

                Map<Integer, List<Usuario>> usuariosPorReposicao =
                        new HashMap<>();

                for (Reposicao reposicao : reposicoes) {

                    List<Usuario> usuarios =
                            usuarioReposicaoService
                                    .listarUsuariosPorReposicao(
                                            reposicao
                                    );

                    usuariosPorReposicao.put(
                            reposicao.getId(),
                            usuarios
                    );
                }


                List<Produto> produtos =
                        produtoService.listarTodos();

                List<Gondola> gondolas =
                        gondolaService.listarTodos();


                request.setAttribute(
                        "reposicoes",
                        reposicoes
                );

                request.setAttribute(
                        "produtos",
                        produtos
                );

                request.setAttribute(
                        "gondolas",
                        gondolas
                );

                request.setAttribute(
                        "usuariosPorReposicao",
                        usuariosPorReposicao
                );


                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/reposicoes.jsp"
                ).forward(request, response);

                return;
            }


            // ==========================
            // CRIAR REPOSIÇÃO
            // ==========================

            Reposicao reposicao =
                    new Reposicao(
                            produto,
                            data,
                            quantidade,
                            motivoReposicao,
                            gondola
                    );

            reposicaoService.cadastrar(
                    reposicao
            );
        }


        // ==========================
        // REDIRECIONAR
        // ==========================

        response.sendRedirect(
                request.getContextPath() + "/admin/reposicoes"
        );
    }
}