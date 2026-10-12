
package com.kairos.servlet;

import com.kairos.model.Compra;
import com.kairos.model.CompraProduto;
import com.kairos.service.CompraService;
import com.kairos.service.CompraProdutoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/compras")
public class CompraServlet extends HttpServlet {

    private final CompraService compraService = new CompraService();

    private final CompraProdutoService compraProdutoService = new CompraProdutoService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");


        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idCompra = Integer.parseInt(id);

                compraService.deletarPorId(idCompra);
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/compras"
            );

            return;
        }


        String busca = request.getParameter("busca");

        List<Compra> compras;

        if (busca != null && !busca.isBlank()) {

            compras = compraService.pesquisar(busca.trim());

        } else {

            compras = compraService.listarTodos();
        }


        Map<Integer, List<CompraProduto>> produtosPorCompra =
                new HashMap<>();

        List<CompraProduto> todosProdutos =
                compraProdutoService.listarTodos();

        for (Compra compra : compras) {

            List<CompraProduto> produtos = todosProdutos.stream()
                    .filter(compraProduto ->
                            compraProduto.getCompra().getId()
                                    == compra.getId()
                    )
                    .collect(Collectors.toList());

            produtosPorCompra.put(
                    compra.getId(),
                    produtos
            );
        }


        // ==========================
        // ERROS AO VINCULAR PRODUTO
        // ==========================

        @SuppressWarnings("unchecked")
        Map<String, String> errosVincular =
                (Map<String, String>) request.getSession()
                        .getAttribute("errosVincular");

        Integer idCompraVincular =
                (Integer) request.getSession()
                        .getAttribute("idCompraVincular");

        String idProdutoVincular =
                (String) request.getSession()
                        .getAttribute("idProdutoVincular");

        String quantidadeItemVincular =
                (String) request.getSession()
                        .getAttribute("quantidadeItemVincular");


        if (errosVincular != null) {

            request.setAttribute(
                    "errosVincular",
                    errosVincular
            );

            request.setAttribute(
                    "idCompraVincular",
                    idCompraVincular
            );

            request.setAttribute(
                    "idProdutoVincular",
                    idProdutoVincular
            );

            request.setAttribute(
                    "quantidadeItemVincular",
                    quantidadeItemVincular
            );


            request.getSession()
                    .removeAttribute("errosVincular");

            request.getSession()
                    .removeAttribute("idCompraVincular");

            request.getSession()
                    .removeAttribute("idProdutoVincular");

            request.getSession()
                    .removeAttribute("quantidadeItemVincular");
        }


        // ==========================
        // ERROS AO ATUALIZAR PRODUTO
        // ==========================

        @SuppressWarnings("unchecked")
        Map<String, String> errosAtualizarProduto =
                (Map<String, String>) request.getSession()
                        .getAttribute("errosAtualizarProduto");

        String idCompraProdutoAtualizar =
                (String) request.getSession()
                        .getAttribute("idCompraProdutoAtualizar");

        String quantidadeItemAtualizar =
                (String) request.getSession()
                        .getAttribute("quantidadeItemAtualizar");


        if (errosAtualizarProduto != null) {

            request.setAttribute(
                    "errosAtualizarProduto",
                    errosAtualizarProduto
            );

            request.setAttribute(
                    "idCompraProdutoAtualizar",
                    idCompraProdutoAtualizar
            );

            request.setAttribute(
                    "quantidadeItemAtualizar",
                    quantidadeItemAtualizar
            );


            request.getSession()
                    .removeAttribute("errosAtualizarProduto");

            request.getSession()
                    .removeAttribute("idCompraProdutoAtualizar");

            request.getSession()
                    .removeAttribute("quantidadeItemAtualizar");
        }


        // ==========================
        // ERROS AO DESVINCULAR PRODUTO
        // ==========================

        String erroDesvincularProduto =
                (String) request.getSession()
                        .getAttribute("erroDesvincularProduto");

        Integer idCompraDesvincular =
                (Integer) request.getSession()
                        .getAttribute("idCompraDesvincular");

        String idCompraProdutoDesvincular =
                (String) request.getSession()
                        .getAttribute("idCompraProdutoDesvincular");


        if (erroDesvincularProduto != null) {

            request.setAttribute(
                    "erroDesvincularProduto",
                    erroDesvincularProduto
            );

            request.setAttribute(
                    "idCompraDesvincular",
                    idCompraDesvincular
            );

            request.setAttribute(
                    "idCompraProdutoDesvincular",
                    idCompraProdutoDesvincular
            );


            request.getSession()
                    .removeAttribute("erroDesvincularProduto");

            request.getSession()
                    .removeAttribute("idCompraDesvincular");

            request.getSession()
                    .removeAttribute("idCompraProdutoDesvincular");
        }


        request.setAttribute(
                "compras",
                compras
        );

        request.setAttribute(
                "produtosPorCompra",
                produtosPorCompra
        );


        request.getRequestDispatcher(
                "/WEB-INF/views/admin/compras.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String id = request.getParameter("id");
        String dataCompra = request.getParameter("dataCompra");

        Map<String, String> erros = new HashMap<>();

        Timestamp dataCompraConvertida = null;


        // ==========================
        // DATA DA COMPRA
        // ==========================

        if (dataCompra == null || dataCompra.isBlank()) {

            erros.put(
                    "dataCompra",
                    "Data da compra é obrigatória"
            );

        } else {

            try {

                LocalDateTime dataConvertida =
                        LocalDateTime.parse(dataCompra);

                if (dataConvertida.isAfter(LocalDateTime.now())) {

                    erros.put(
                            "dataCompra",
                            "A data da compra não pode ser futura"
                    );

                } else {

                    dataCompraConvertida =
                            Timestamp.valueOf(dataConvertida);
                }

            } catch (DateTimeParseException e) {

                erros.put(
                        "dataCompra",
                        "Data da compra inválida"
                );
            }
        }


        if (id != null && !id.isBlank()) {

            // ==========================
            // ATUALIZAR COMPRA
            // ==========================

            int idCompra = Integer.parseInt(id);

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "errosAtualizacao",
                        erros
                );

                request.setAttribute(
                        "idCompraAtualizacao",
                        idCompra
                );

                carregarCompras(request);

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/compras.jsp"
                ).forward(request, response);

                return;
            }


            Compra compra = compraService.buscarPorId(idCompra);

            compra.setDataCompra(dataCompraConvertida);

            compraService.atualizar(compra);

        } else {

            // ==========================
            // ADICIONAR COMPRA
            // ==========================

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "erros",
                        erros
                );

                carregarCompras(request);

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/compras.jsp"
                ).forward(request, response);

                return;
            }


            Compra compra = new Compra(
                    dataCompraConvertida
            );

            compraService.cadastrar(compra);
        }


        response.sendRedirect(
                request.getContextPath() + "/admin/compras"
        );
    }


    // ==========================
    // CARREGAR COMPRAS
    // ==========================

    private void carregarCompras(HttpServletRequest request) {

        List<Compra> compras =
                compraService.listarTodos();

        Map<Integer, List<CompraProduto>> produtosPorCompra =
                new HashMap<>();

        List<CompraProduto> todosProdutos =
                compraProdutoService.listarTodos();

        for (Compra compra : compras) {

            List<CompraProduto> produtos = todosProdutos.stream()
                    .filter(compraProduto ->
                            compraProduto.getCompra().getId()
                                    == compra.getId()
                    )
                    .collect(Collectors.toList());

            produtosPorCompra.put(
                    compra.getId(),
                    produtos
            );
        }


        request.setAttribute(
                "compras",
                compras
        );

        request.setAttribute(
                "produtosPorCompra",
                produtosPorCompra
        );
    }
}
