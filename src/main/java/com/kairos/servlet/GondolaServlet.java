package com.kairos.servlet;

import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.model.Setor;
import com.kairos.service.GondolaProdutoService;
import com.kairos.service.GondolaService;
import com.kairos.service.SetorService;
import com.kairos.utils.exceptions.notfound.SetorNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/gondolas")
public class GondolaServlet extends HttpServlet {

    private final GondolaService gondolaService =
            new GondolaService();

    private final GondolaProdutoService gondolaProdutoService =
            new GondolaProdutoService();

    private final SetorService setorService =
            new SetorService();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");


        // ==========================
        // DELETAR GÔNDOLA
        // ==========================

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idGondola = Integer.parseInt(id);

                gondolaService.deletarPorId(idGondola);
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/gondolas"
            );

            return;
        }


        // ==========================
        // LISTAR / PESQUISAR GÔNDOLAS
        // ==========================

        String busca = request.getParameter("busca");

        List<Gondola> gondolas;

        if (busca != null && !busca.isBlank()) {
            gondolas = gondolaService.pesquisar(busca.trim());
        } else {
            gondolas = gondolaService.listarTodos();
        }


        // ==========================
        // PRODUTOS POR GÔNDOLA
        // ==========================

        Map<Integer, List<Produto>> produtosPorGondola =
                new HashMap<>();

        for (Gondola gondola : gondolas) {

            List<Produto> produtos =
                    gondolaProdutoService
                            .listarProdutosPorGondola(gondola);

            produtosPorGondola.put(
                    gondola.getId(),
                    produtos
            );
        }


        // ==========================
        // RECUPERAR ERRO DE VINCULAR PRODUTO
        // ==========================

        HttpSession session = request.getSession();

        Object erroVincularProduto =
                session.getAttribute("erroVincularProduto");

        Object idGondolaVincularProduto =
                session.getAttribute("idGondolaVincularProduto");

        Object nomeProdutoVincular =
                session.getAttribute("nomeProdutoVincular");

        if (erroVincularProduto != null) {

            request.setAttribute(
                    "erroVincularProduto",
                    erroVincularProduto
            );

            request.setAttribute(
                    "idGondolaVincularProduto",
                    idGondolaVincularProduto
            );

            request.setAttribute(
                    "nomeProdutoVincular",
                    nomeProdutoVincular
            );
        }

        session.removeAttribute("erroVincularProduto");
        session.removeAttribute("idGondolaVincularProduto");
        session.removeAttribute("nomeProdutoVincular");


        // ==========================
        // ATRIBUTOS
        // ==========================

        request.setAttribute(
                "gondolas",
                gondolas
        );

        request.setAttribute(
                "produtosPorGondola",
                produtosPorGondola
        );


        // ==========================
        // FORWARD
        // ==========================

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/gondolas.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String id = request.getParameter("id");

        String capacidadeMaxima =
                request.getParameter("capacidadeMaxima");

        String nomeSetor =
                request.getParameter("nomeSetor");


        // ==========================
        // ATUALIZAR GÔNDOLA
        // ==========================

        if (id != null) {

            int idGondola = Integer.parseInt(id);

            Gondola gondola =
                    gondolaService.buscarPorId(idGondola);

            Map<String, String> erros =
                    new HashMap<>();


            // ==========================
            // CAPACIDADE MÁXIMA
            // ==========================

            int capacidade = 0;

            if (capacidadeMaxima == null
                    || capacidadeMaxima.isBlank()) {

                erros.put(
                        "capacidadeMaxima",
                        "Capacidade máxima é obrigatória"
                );

            } else {

                try {

                    capacidade =
                            Integer.parseInt(capacidadeMaxima);

                    if (capacidade < 1) {

                        erros.put(
                                "capacidadeMaxima",
                                "Capacidade máxima deve ser maior que 0"
                        );
                    }

                } catch (NumberFormatException e) {

                    erros.put(
                            "capacidadeMaxima",
                            "Capacidade máxima deve ser um número válido"
                    );
                }
            }


            // ==========================
            // SETOR
            // ==========================

            Setor setor = null;

            if (nomeSetor == null || nomeSetor.isBlank()) {

                erros.put(
                        "setor",
                        "Nome do setor é obrigatório"
                );

            } else {

                try {

                    setor =
                            setorService.buscarPorNome(
                                    nomeSetor.trim()
                            );

                } catch (SetorNotFoundException e) {

                    erros.put(
                            "setor",
                            "Setor não encontrado"
                    );
                }
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
                        "idGondolaAtualizacao",
                        idGondola
                );


                List<Gondola> gondolas =
                        gondolaService.listarTodos();

                Map<Integer, List<Produto>> produtosPorGondola =
                        new HashMap<>();

                for (Gondola gondolaLista : gondolas) {

                    List<Produto> produtos =
                            gondolaProdutoService
                                    .listarProdutosPorGondola(
                                            gondolaLista
                                    );

                    produtosPorGondola.put(
                            gondolaLista.getId(),
                            produtos
                    );
                }


                request.setAttribute(
                        "gondolas",
                        gondolas
                );

                request.setAttribute(
                        "produtosPorGondola",
                        produtosPorGondola
                );


                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/gondolas.jsp"
                ).forward(request, response);

                return;
            }


            // ==========================
            // ATUALIZAR
            // ==========================

            gondola.setCapacidadeMaxima(capacidade);
            gondola.setSetor(setor);

            gondolaService.atualizar(gondola);

        } else {


            // ==========================
            // ADICIONAR GÔNDOLA
            // ==========================

            Map<String, String> erros =
                    new HashMap<>();


            // ==========================
            // CAPACIDADE MÁXIMA
            // ==========================

            int capacidade = 0;

            if (capacidadeMaxima == null
                    || capacidadeMaxima.isBlank()) {

                erros.put(
                        "capacidadeMaxima",
                        "Capacidade máxima é obrigatória"
                );

            } else {

                try {

                    capacidade =
                            Integer.parseInt(capacidadeMaxima);

                    if (capacidade < 1) {

                        erros.put(
                                "capacidadeMaxima",
                                "Capacidade máxima deve ser maior que 0"
                        );
                    }

                } catch (NumberFormatException e) {

                    erros.put(
                            "capacidadeMaxima",
                            "Capacidade máxima deve ser um número válido"
                    );
                }
            }


            // ==========================
            // SETOR
            // ==========================

            Setor setor = null;

            if (nomeSetor == null || nomeSetor.isBlank()) {

                erros.put(
                        "setor",
                        "Nome do setor é obrigatório"
                );

            } else {

                try {

                    setor =
                            setorService.buscarPorNome(
                                    nomeSetor.trim()
                            );

                } catch (SetorNotFoundException e) {

                    erros.put(
                            "setor",
                            "Setor não encontrado"
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


                List<Gondola> gondolas =
                        gondolaService.listarTodos();

                Map<Integer, List<Produto>> produtosPorGondola =
                        new HashMap<>();

                for (Gondola gondola : gondolas) {

                    List<Produto> produtos =
                            gondolaProdutoService
                                    .listarProdutosPorGondola(
                                            gondola
                                    );

                    produtosPorGondola.put(
                            gondola.getId(),
                            produtos
                    );
                }


                request.setAttribute(
                        "gondolas",
                        gondolas
                );

                request.setAttribute(
                        "produtosPorGondola",
                        produtosPorGondola
                );


                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/gondolas.jsp"
                ).forward(request, response);

                return;
            }


            // ==========================
            // CRIAR GÔNDOLA
            // ==========================

            Gondola gondola = new Gondola(
                    capacidade,
                    setor
            );

            gondolaService.cadastrar(gondola);
        }


        // ==========================
        // REDIRECT
        // ==========================

        response.sendRedirect(
                request.getContextPath() + "/admin/gondolas"
        );
    }
}