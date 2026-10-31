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

    private final ReposicaoService reposicaoService = new ReposicaoService();
    private final ProdutoService produtoService = new ProdutoService();
    private final GondolaService gondolaService = new GondolaService();
    private final UsuarioReposicaoService usuarioReposicaoService = new UsuarioReposicaoService();

    // Processa a exclusão, pesquisa e listagem de reposições
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idReposicao = Integer.parseInt(id);

                reposicaoService.deletarPorId(idReposicao);
            }

            response.sendRedirect(request.getContextPath() + "/admin/reposicoes");

            return;
        }

        String busca = request.getParameter("busca");

        List<Reposicao> reposicoes;

        if (busca != null && !busca.isBlank()) {
            reposicoes = reposicaoService.pesquisar(busca.trim());
        } else {
            reposicoes = reposicaoService.listarTodos();
        }

        // Recupera os usuários vinculados a cada reposição
        Map<Integer, List<Usuario>> usuariosPorReposicao = new HashMap<>();

        for (Reposicao reposicao : reposicoes) {

            List<Usuario> usuarios = usuarioReposicaoService.listarUsuariosPorReposicao(reposicao);

            usuariosPorReposicao.put(reposicao.getId(), usuarios);
        }

        // Recupera os produtos e as gôndolas disponíveis
        List<Produto> produtos = produtoService.listarTodos();
        List<Gondola> gondolas = gondolaService.listarTodos();

        // Recupera possíveis erros de vinculação armazenados na sessão
        HttpSession session = request.getSession();

        String erroVincularReposicao = (String) session.getAttribute("erroVincularReposicao");
        Object idUsuarioVincular = session.getAttribute("idUsuarioVincular");
        Object idReposicaoVincular = session.getAttribute("idReposicaoVincular");

        request.setAttribute("erroVincularReposicao", erroVincularReposicao);
        request.setAttribute("idUsuarioVincular", idUsuarioVincular);
        request.setAttribute("idReposicaoVincular", idReposicaoVincular);

        session.removeAttribute("erroVincularReposicao");
        session.removeAttribute("idUsuarioVincular");
        session.removeAttribute("idReposicaoVincular");

        request.setAttribute("reposicoes", reposicoes);
        request.setAttribute("produtos", produtos);
        request.setAttribute("gondolas", gondolas);
        request.setAttribute("usuariosPorReposicao", usuariosPorReposicao);

        request.getRequestDispatcher("/WEB-INF/views/admin/reposicoes.jsp")
                .forward(request, response);
    }

    // Processa o cadastro ou a atualização de reposições
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String quantidadeReposto = request.getParameter("quantidadeReposto");
        String motivoReposicao = request.getParameter("motivoReposicao");

        if (id != null) {

            // Atualiza os dados de uma reposição existente
            int idReposicao = Integer.parseInt(id);

            Reposicao reposicao = reposicaoService.buscarPorId(idReposicao);

            Map<String, String> erros = new HashMap<>();

            int quantidade = 0;

            // Valida a quantidade reposta
            if (quantidadeReposto == null || quantidadeReposto.isBlank()) {

                erros.put("quantidadeReposto", "Quantidade reposta é obrigatória");

            } else {

                try {

                    long quantidadeConvertida = Long.parseLong(quantidadeReposto);

                    if (quantidadeConvertida > Integer.MAX_VALUE) {

                        erros.put("quantidadeReposto", "Quantidade reposta não pode ultrapassar 2.147.483.647");

                    } else if (quantidadeConvertida < 1) {

                        erros.put("quantidadeReposto", "Quantidade reposta deve ser de pelo menos 1");

                    } else {

                        quantidade = (int) quantidadeConvertida;
                    }

                } catch (NumberFormatException e) {
                    erros.put("quantidadeReposto", "Quantidade reposta deve ser um número inteiro válido");
                }
            }

            // Valida os campos permitidos na atualização
            if (motivoReposicao == null || motivoReposicao.isBlank()) {
                erros.put("motivoReposicao", "Motivo da reposição é obrigatório");
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("errosAtualizacao", erros);
                request.setAttribute("idReposicaoAtualizacao", idReposicao);

                List<Reposicao> reposicoes = reposicaoService.listarTodos();
                Map<Integer, List<Usuario>> usuariosPorReposicao = new HashMap<>();

                for (Reposicao reposicaoLista : reposicoes) {

                    List<Usuario> usuarios = usuarioReposicaoService.listarUsuariosPorReposicao(reposicaoLista);

                    usuariosPorReposicao.put(reposicaoLista.getId(), usuarios);
                }

                List<Produto> produtos = produtoService.listarTodos();
                List<Gondola> gondolas = gondolaService.listarTodos();

                request.setAttribute("reposicoes", reposicoes);
                request.setAttribute("produtos", produtos);
                request.setAttribute("gondolas", gondolas);
                request.setAttribute("usuariosPorReposicao", usuariosPorReposicao);

                request.getRequestDispatcher("/WEB-INF/views/admin/reposicoes.jsp")
                        .forward(request, response);

                return;
            }

            // Mantém o produto, a data e a gôndola originais
            reposicao.setQuantidadeReposto(quantidade);
            reposicao.setMotivoReposicao(motivoReposicao);

            reposicaoService.atualizar(reposicao);

        } else {

            // Cadastra uma nova reposição
            String produtoId = request.getParameter("produtoId");
            String dataReposicao = request.getParameter("dataReposicao");
            String gondolaId = request.getParameter("gondolaId");

            Map<String, String> erros = new HashMap<>();

            Produto produto = null;
            Gondola gondola = null;
            Timestamp data = null;

            int quantidade = 0;

            // Valida o produto informado
            if (produtoId == null || produtoId.isBlank()) {

                erros.put("produtoId", "Produto é obrigatório");

            } else {

                try {

                    int idProduto = Integer.parseInt(produtoId);

                    produto = produtoService.buscarPorId(idProduto);

                } catch (NumberFormatException e) {
                    erros.put("produtoId", "Produto deve ser um número válido");
                }
            }

            // Converte e valida a data da reposição
            if (dataReposicao == null || dataReposicao.isBlank()) {

                erros.put("dataReposicao", "Data da reposição é obrigatória");

            } else {

                try {

                    data = Timestamp.valueOf(dataReposicao.replace("T", " ") + ":00");

                } catch (IllegalArgumentException e) {
                    erros.put("dataReposicao", "Data da reposição inválida");
                }
            }

            // Valida a quantidade reposta
            if (quantidadeReposto == null || quantidadeReposto.isBlank()) {

                erros.put("quantidadeReposto", "Quantidade reposta é obrigatória");

            } else {

                try {

                    long quantidadeConvertida = Long.parseLong(quantidadeReposto);

                    if (quantidadeConvertida > Integer.MAX_VALUE) {

                        erros.put("quantidadeReposto", "Quantidade reposta não pode ultrapassar 2.147.483.647");

                    } else if (quantidadeConvertida < 1) {

                        erros.put("quantidadeReposto", "Quantidade reposta deve ser de pelo menos 1");

                    } else {

                        quantidade = (int) quantidadeConvertida;
                    }

                } catch (NumberFormatException e) {
                    erros.put("quantidadeReposto", "Quantidade reposta deve ser um número inteiro válido");
                }
            }

            // Valida o motivo da reposição
            if (motivoReposicao == null || motivoReposicao.isBlank()) {
                erros.put("motivoReposicao", "Motivo da reposição é obrigatório");
            }

            // Valida a gôndola informada
            if (gondolaId == null || gondolaId.isBlank()) {

                erros.put("gondolaId", "Gôndola é obrigatória");

            } else {

                try {

                    int idGondola = Integer.parseInt(gondolaId);

                    gondola = gondolaService.buscarPorId(idGondola);

                } catch (NumberFormatException e) {
                    erros.put("gondolaId", "Gôndola deve ser um número válido");
                }
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("erros", erros);

                List<Reposicao> reposicoes = reposicaoService.listarTodos();
                Map<Integer, List<Usuario>> usuariosPorReposicao = new HashMap<>();

                for (Reposicao reposicaoLista : reposicoes) {

                    List<Usuario> usuarios = usuarioReposicaoService.listarUsuariosPorReposicao(reposicaoLista);

                    usuariosPorReposicao.put(reposicaoLista.getId(), usuarios);
                }

                List<Produto> produtos = produtoService.listarTodos();
                List<Gondola> gondolas = gondolaService.listarTodos();

                request.setAttribute("reposicoes", reposicoes);
                request.setAttribute("produtos", produtos);
                request.setAttribute("gondolas", gondolas);
                request.setAttribute("usuariosPorReposicao", usuariosPorReposicao);

                request.getRequestDispatcher("/WEB-INF/views/admin/reposicoes.jsp")
                        .forward(request, response);

                return;
            }

            // Cadastra a reposição com os dados validados
            Reposicao reposicao = new Reposicao(
                    produto,
                    data,
                    quantidade,
                    motivoReposicao,
                    gondola
            );

            reposicaoService.cadastrar(reposicao);
        }

        response.sendRedirect(request.getContextPath() + "/admin/reposicoes");
    }
}
