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

    private final GondolaService gondolaService = new GondolaService();
    private final GondolaProdutoService gondolaProdutoService = new GondolaProdutoService();
    private final SetorService setorService = new SetorService();

    // Processa a exclusão, pesquisa e listagem de gôndolas
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idGondola = Integer.parseInt(id);

                gondolaService.deletarPorId(idGondola);
            }

            response.sendRedirect(request.getContextPath() + "/admin/gondolas");

            return;
        }

        String busca = request.getParameter("busca");

        List<Gondola> gondolas;

        if (busca != null && !busca.isBlank()) {
            gondolas = gondolaService.pesquisar(busca.trim());
        } else {
            gondolas = gondolaService.listarTodos();
        }

        // Recupera os produtos vinculados a cada gôndola
        Map<Integer, List<Produto>> produtosPorGondola = new HashMap<>();

        for (Gondola gondola : gondolas) {

            List<Produto> produtos = gondolaProdutoService.listarProdutosPorGondola(gondola);

            produtosPorGondola.put(gondola.getId(), produtos);
        }

        // Recupera possíveis erros de vinculação armazenados na sessão
        HttpSession session = request.getSession();

        String erroProdutoVincular = (String) session.getAttribute("erroProdutoVincular");
        String erroMarcaVincular = (String) session.getAttribute("erroMarcaVincular");
        Object idGondolaVincularProduto = session.getAttribute("idGondolaVincularProduto");
        Object nomeProdutoVincular = session.getAttribute("nomeProdutoVincular");
        Object marcaProdutoVincular = session.getAttribute("marcaProdutoVincular");

        request.setAttribute("erroProdutoVincular", erroProdutoVincular);
        request.setAttribute("erroMarcaVincular", erroMarcaVincular);
        request.setAttribute("idGondolaVincularProduto", idGondolaVincularProduto);
        request.setAttribute("nomeProdutoVincular", nomeProdutoVincular);
        request.setAttribute("marcaProdutoVincular", marcaProdutoVincular);

        session.removeAttribute("erroProdutoVincular");
        session.removeAttribute("erroMarcaVincular");
        session.removeAttribute("idGondolaVincularProduto");
        session.removeAttribute("nomeProdutoVincular");
        session.removeAttribute("marcaProdutoVincular");

        request.setAttribute("gondolas", gondolas);
        request.setAttribute("produtosPorGondola", produtosPorGondola);

        request.getRequestDispatcher("/WEB-INF/views/admin/gondolas.jsp")
                .forward(request, response);
    }

    // Processa o cadastro ou a atualização de gôndolas
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String capacidadeMaxima = request.getParameter("capacidadeMaxima");

        if (id != null) {

            // Atualiza a capacidade máxima de uma gôndola existente
            int idGondola = Integer.parseInt(id);

            Gondola gondola = gondolaService.buscarPorId(idGondola);

            Map<String, String> erros = new HashMap<>();

            int capacidade = 0;

            // Valida a capacidade máxima informada
            if (capacidadeMaxima == null || capacidadeMaxima.isBlank()) {

                erros.put("capacidadeMaxima", "Capacidade máxima é obrigatória");

            } else {

                try {

                    capacidade = Integer.parseInt(capacidadeMaxima);

                    if (capacidade < 1) {
                        erros.put("capacidadeMaxima", "Capacidade máxima deve ser maior que 0");
                    }

                } catch (NumberFormatException e) {
                    erros.put("capacidadeMaxima", "Capacidade máxima deve ser um número válido");
                }
            }

            // Retorna ao formulário caso existam erros de atualização
            if (!erros.isEmpty()) {

                request.setAttribute("errosAtualizacao", erros);
                request.setAttribute("idGondolaAtualizacao", idGondola);

                List<Gondola> gondolas = gondolaService.listarTodos();
                Map<Integer, List<Produto>> produtosPorGondola = new HashMap<>();

                for (Gondola gondolaLista : gondolas) {

                    List<Produto> produtos = gondolaProdutoService.listarProdutosPorGondola(gondolaLista);

                    produtosPorGondola.put(gondolaLista.getId(), produtos);
                }

                request.setAttribute("gondolas", gondolas);
                request.setAttribute("produtosPorGondola", produtosPorGondola);

                request.getRequestDispatcher("/WEB-INF/views/admin/gondolas.jsp")
                        .forward(request, response);

                return;
            }

            // Mantém o setor original e atualiza somente a capacidade máxima
            gondola.setCapacidadeMaxima(capacidade);

            gondolaService.atualizar(gondola);

        } else {

            // Cadastra uma nova gôndola
            String nomeSetor = request.getParameter("nomeSetor");

            Map<String, String> erros = new HashMap<>();

            int capacidade = 0;

            // Valida a capacidade máxima informada
            if (capacidadeMaxima == null || capacidadeMaxima.isBlank()) {

                erros.put("capacidadeMaxima", "Capacidade máxima é obrigatória");

            } else {

                try {

                    capacidade = Integer.parseInt(capacidadeMaxima);

                    if (capacidade < 1) {
                        erros.put("capacidadeMaxima", "Capacidade máxima deve ser maior que 0");
                    }

                } catch (NumberFormatException e) {
                    erros.put("capacidadeMaxima", "Capacidade máxima deve ser um número válido");
                }
            }

            // Busca e valida o setor informado
            Setor setor = null;

            if (nomeSetor == null || nomeSetor.isBlank()) {

                erros.put("setor", "Nome do setor é obrigatório");

            } else {

                try {

                    setor = setorService.buscarPorNome(nomeSetor.trim());

                } catch (SetorNotFoundException e) {
                    erros.put("setor", "Setor não encontrado");
                }
            }

            // Retorna ao formulário caso existam erros de cadastro
            if (!erros.isEmpty()) {

                request.setAttribute("erros", erros);

                List<Gondola> gondolas = gondolaService.listarTodos();
                Map<Integer, List<Produto>> produtosPorGondola = new HashMap<>();

                for (Gondola gondola : gondolas) {

                    List<Produto> produtos = gondolaProdutoService.listarProdutosPorGondola(gondola);

                    produtosPorGondola.put(gondola.getId(), produtos);
                }

                request.setAttribute("gondolas", gondolas);
                request.setAttribute("produtosPorGondola", produtosPorGondola);

                request.getRequestDispatcher("/WEB-INF/views/admin/gondolas.jsp")
                        .forward(request, response);

                return;
            }

            // Cria e cadastra a gôndola validada
            Gondola gondola = new Gondola(capacidade, setor);

            gondolaService.cadastrar(gondola);
        }

        response.sendRedirect(request.getContextPath() + "/admin/gondolas");
    }
}
