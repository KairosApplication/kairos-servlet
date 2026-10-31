package com.kairos.servlet;

import com.kairos.model.Produto;
import com.kairos.service.ProdutoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/produtos")
public class ProdutoServlet extends HttpServlet {

    private final ProdutoService produtoService = new ProdutoService();

    // Processa a exclusão, pesquisa e listagem de produtos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idProduto = Integer.parseInt(id);

                produtoService.deletarPorId(idProduto);
            }

            response.sendRedirect(request.getContextPath() + "/admin/produtos");

            return;
        }

        String busca = request.getParameter("busca");

        List<Produto> produtos;

        if (busca != null && !busca.isBlank()) {
            produtos = produtoService.pesquisar(busca.trim());
        } else {
            produtos = produtoService.listarTodos();
        }

        request.setAttribute("produtos", produtos);

        request.getRequestDispatcher("/WEB-INF/views/admin/produtos.jsp")
                .forward(request, response);
    }

    // Processa o cadastro ou a atualização de produtos
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        String marca = request.getParameter("marca");
        String nome = request.getParameter("nome");
        String quantidadeEstoqueParam = request.getParameter("quantidadeEstoque");

        if (id != null) {

            // Atualiza o estoque de um produto existente
            int idProduto = Integer.parseInt(id);

            Produto produto = produtoService.buscarPorId(idProduto);

            Map<String, String> erros = new HashMap<>();

            int quantidadeEstoque = 0;

            // Valida a quantidade em estoque
            if (quantidadeEstoqueParam == null || quantidadeEstoqueParam.isBlank()) {

                erros.put("quantidadeEstoque", "Quantidade no estoque é obrigatória");

            } else {

                try {

                    quantidadeEstoque = Integer.parseInt(quantidadeEstoqueParam);

                    if (quantidadeEstoque < 0) {
                        erros.put("quantidadeEstoque", "A quantidade no estoque não pode ser menor que 0");
                    }

                } catch (NumberFormatException e) {
                    erros.put("quantidadeEstoque", "Quantidade no estoque deve ser um número válido");
                }
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("errosAtualizacao", erros);
                request.setAttribute("idProdutoAtualizacao", idProduto);

                List<Produto> produtos = produtoService.listarTodos();

                request.setAttribute("produtos", produtos);

                request.getRequestDispatcher("/WEB-INF/views/admin/produtos.jsp")
                        .forward(request, response);

                return;
            }

            // Mantém a marca e o nome originais do produto
            produto.setQuantidadeEstoque(quantidadeEstoque);

            produtoService.atualizar(produto);

        } else {

            // Cadastra um novo produto
            Map<String, String> erros = new HashMap<>();

            int quantidadeEstoque = 0;

            // Valida os campos obrigatórios do cadastro
            if (marca == null || marca.isBlank()) {
                erros.put("marca", "Marca é obrigatória");
            }

            if (nome == null || nome.isBlank()) {

                erros.put("nome", "Nome é obrigatório");

            } else if (marca != null && !marca.isBlank()
                    && produtoService.existePorNomeEMarca(nome, marca)) {

                erros.put("nome", "Produto já cadastrado para esta marca");
            }

            // Converte e valida a quantidade em estoque
            if (quantidadeEstoqueParam == null || quantidadeEstoqueParam.isBlank()) {

                erros.put("quantidadeEstoque", "Quantidade no estoque é obrigatória");

            } else {

                try {

                    quantidadeEstoque = Integer.parseInt(quantidadeEstoqueParam);

                    if (quantidadeEstoque < 0) {
                        erros.put("quantidadeEstoque", "A quantidade no estoque não pode ser menor que 0");
                    }

                } catch (NumberFormatException e) {
                    erros.put("quantidadeEstoque", "Quantidade no estoque deve ser um número válido");
                }
            }

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                request.setAttribute("erros", erros);

                List<Produto> produtos = produtoService.listarTodos();

                request.setAttribute("produtos", produtos);

                request.getRequestDispatcher("/WEB-INF/views/admin/produtos.jsp")
                        .forward(request, response);

                return;
            }

            // Cadastra o produto validado
            Produto produto = new Produto(
                    marca,
                    nome,
                    quantidadeEstoque
            );

            produtoService.cadastrar(produto);
        }

        response.sendRedirect(request.getContextPath() + "/admin/produtos");
    }
}
