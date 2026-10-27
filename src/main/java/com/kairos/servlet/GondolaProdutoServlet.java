package com.kairos.servlet;

import com.kairos.model.Gondola;
import com.kairos.model.GondolaProduto;
import com.kairos.model.Produto;
import com.kairos.service.GondolaProdutoService;
import com.kairos.service.GondolaService;
import com.kairos.service.ProdutoService;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/gondolas-produtos")
public class GondolaProdutoServlet extends HttpServlet {

    private final GondolaProdutoService gondolaProdutoService = new GondolaProdutoService();
    private final GondolaService gondolaService = new GondolaService();
    private final ProdutoService produtoService = new ProdutoService();

    // Processa a vinculação e desvinculação de produtos às gôndolas
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int idGondola = Integer.parseInt(request.getParameter("idGondola"));

        String nomeProduto = request.getParameter("nomeProduto");
        String marcaProduto = request.getParameter("marcaProduto");

        try {

            Gondola gondola = gondolaService.buscarPorId(idGondola);

            Map<String, String> erros = new HashMap<>();

            // Valida o nome do produto informado
            if (nomeProduto == null || nomeProduto.isBlank()) {

                erros.put("produto", "Nome do produto é obrigatório");

            } else if (!produtoService.existePorNome(nomeProduto)) {
                erros.put("produto", "Produto não encontrado");
            }

            // Valida a marca do produto informado
            if (marcaProduto == null || marcaProduto.isBlank()) {

                erros.put("marca", "Marca do produto é obrigatória");

            } else if (!produtoService.existePorMarca(marcaProduto)) {
                erros.put("marca", "Marca não encontrada");
            }

            // Armazena os erros de validação na sessão
            if (!erros.isEmpty()) {

                request.getSession().setAttribute("erroProdutoVincular", erros.get("produto"));
                request.getSession().setAttribute("erroMarcaVincular", erros.get("marca"));
                request.getSession().setAttribute("idGondolaVincularProduto", idGondola);
                request.getSession().setAttribute("nomeProdutoVincular", nomeProduto);
                request.getSession().setAttribute("marcaProdutoVincular", marcaProduto);

                response.sendRedirect(request.getContextPath() + "/admin/gondolas");

                return;
            }

            // Busca o produto e cria o relacionamento com a gôndola
            Produto produto = produtoService.buscarPorNomeEMarca(nomeProduto, marcaProduto);

            GondolaProduto gondolaProduto = new GondolaProduto(gondola, produto);

            // Executa a vinculação ou desvinculação solicitada
            if ("vincular".equals(acao)) {

                gondolaProdutoService.vincular(gondolaProduto);

            } else if ("desvincular".equals(acao)) {

                gondolaProdutoService.desvincular(gondolaProduto);

            } else {

                throw new ServletException("Ação inválida");
            }

        } catch (ServiceException | GondolaNotFoundException | ProdutoNotFoundException e) {

            // Armazena os erros da operação na sessão
            request.getSession().setAttribute("erroProdutoVincular", e.getMessage());
            request.getSession().setAttribute("idGondolaVincularProduto", idGondola);
            request.getSession().setAttribute("nomeProdutoVincular", nomeProduto);
            request.getSession().setAttribute("marcaProdutoVincular", marcaProduto);
        }

        response.sendRedirect(request.getContextPath() + "/admin/gondolas");
    }
}
