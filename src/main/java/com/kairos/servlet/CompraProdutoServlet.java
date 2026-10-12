
package com.kairos.servlet;

import com.kairos.model.Compra;
import com.kairos.model.CompraProduto;
import com.kairos.model.Produto;
import com.kairos.service.CompraProdutoService;
import com.kairos.service.CompraService;
import com.kairos.service.ProdutoService;
import com.kairos.utils.exceptions.notfound.CompraNotFoundException;
import com.kairos.utils.exceptions.notfound.CompraProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.quantity.PurchaseQuantityException;
import com.kairos.utils.exceptions.system.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/compras-produtos")
public class CompraProdutoServlet extends HttpServlet {

    private final CompraProdutoService compraProdutoService =
            new CompraProdutoService();

    private final CompraService compraService =
            new CompraService();

    private final ProdutoService produtoService =
            new ProdutoService();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        String idCompraParametro = request.getParameter("idCompra");

        int idCompra;

        try {

            idCompra = Integer.parseInt(idCompraParametro);

            if (idCompra <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            throw new ServletException("ID da compra inválido", e);
        }


        if ("vincular".equals(acao)) {

            // ==========================
            // VINCULAR PRODUTO
            // ==========================

            String idProdutoParametro =
                    request.getParameter("idProduto");

            String quantidadeParametro =
                    request.getParameter("quantidadeItem");

            Map<String, String> erros = new HashMap<>();

            Integer idProduto = null;
            Integer quantidadeItem = null;


            // ==========================
            // ID DO PRODUTO
            // ==========================

            if (idProdutoParametro == null
                    || idProdutoParametro.isBlank()) {

                erros.put(
                        "idProduto",
                        "ID do produto é obrigatório"
                );

            } else {

                try {

                    idProduto = Integer.parseInt(idProdutoParametro);

                    if (idProduto <= 0) {

                        erros.put(
                                "idProduto",
                                "ID do produto deve ser maior que 0"
                        );
                    }

                } catch (NumberFormatException e) {

                    erros.put(
                            "idProduto",
                            "ID do produto inválido"
                    );
                }
            }


            // ==========================
            // QUANTIDADE
            // ==========================

            if (quantidadeParametro == null
                    || quantidadeParametro.isBlank()) {

                erros.put(
                        "quantidadeItem",
                        "Quantidade do produto é obrigatória"
                );

            } else {

                try {

                    quantidadeItem = Integer.parseInt(
                            quantidadeParametro
                    );

                    if (quantidadeItem <= 0) {

                        erros.put(
                                "quantidadeItem",
                                "A quantidade deve ser maior que 0"
                        );
                    }

                } catch (NumberFormatException e) {

                    erros.put(
                            "quantidadeItem",
                            "Quantidade do produto inválida"
                    );
                }
            }


            // ==========================
            // VINCULAR
            // ==========================

            if (erros.isEmpty()) {

                try {

                    Compra compra = compraService.buscarPorId(
                            idCompra
                    );

                    Produto produto = produtoService.buscarPorId(
                            idProduto
                    );

                    CompraProduto compraProduto = new CompraProduto(
                            produto,
                            compra,
                            quantidadeItem
                    );

                    compraProdutoService.cadastrar(
                            compraProduto
                    );

                } catch (ProdutoNotFoundException e) {

                    erros.put(
                            "idProduto",
                            e.getMessage()
                    );

                } catch (PurchaseQuantityException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );

                } catch (CompraNotFoundException e) {

                    erros.put(
                            "idProduto",
                            e.getMessage()
                    );

                } catch (ServiceException e) {

                    erros.put(
                            "idProduto",
                            e.getMessage()
                    );
                }
            }


            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.getSession().setAttribute(
                        "errosVincular",
                        erros
                );

                request.getSession().setAttribute(
                        "idCompraVincular",
                        idCompra
                );

                request.getSession().setAttribute(
                        "idProdutoVincular",
                        idProdutoParametro
                );

                request.getSession().setAttribute(
                        "quantidadeItemVincular",
                        quantidadeParametro
                );
            }

        } else if ("atualizar".equals(acao)) {

            // ==========================
            // ATUALIZAR PRODUTO COMPRADO
            // ==========================

            String idCompraProdutoParametro =
                    request.getParameter("idCompraProduto");

            String quantidadeParametro =
                    request.getParameter("quantidadeItem");

            Map<String, String> erros = new HashMap<>();

            Integer idCompraProduto = null;
            Integer quantidadeItem = null;


            // ==========================
            // ID DO PRODUTO COMPRADO
            // ==========================

            try {

                idCompraProduto = Integer.parseInt(
                        idCompraProdutoParametro
                );

                if (idCompraProduto <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {

                erros.put(
                        "quantidadeItem",
                        "ID do produto da compra inválido"
                );
            }


            // ==========================
            // QUANTIDADE
            // ==========================

            if (quantidadeParametro == null
                    || quantidadeParametro.isBlank()) {

                erros.put(
                        "quantidadeItem",
                        "Quantidade do produto é obrigatória"
                );

            } else {

                try {

                    quantidadeItem = Integer.parseInt(
                            quantidadeParametro
                    );

                    if (quantidadeItem <= 0) {

                        erros.put(
                                "quantidadeItem",
                                "A quantidade deve ser maior que 0"
                        );
                    }

                } catch (NumberFormatException e) {

                    erros.put(
                            "quantidadeItem",
                            "Quantidade do produto inválida"
                    );
                }
            }


            // ==========================
            // ATUALIZAR
            // ==========================

            if (erros.isEmpty()) {

                try {

                    CompraProduto compraProduto =
                            compraProdutoService.buscarPorId(
                                    idCompraProduto
                            );

                    if (compraProduto.getCompra().getId() != idCompra) {

                        throw new ServiceException(
                                "O produto não pertence a esta compra"
                        );
                    }

                    // Atualiza somente a quantidade do produto vinculado.

                    compraProduto.setQuantidadeItem(
                            quantidadeItem
                    );

                    compraProdutoService.atualizar(
                            compraProduto
                    );

                } catch (CompraProdutoNotFoundException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );

                } catch (PurchaseQuantityException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );

                } catch (ProdutoNotFoundException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );

                } catch (CompraNotFoundException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );

                } catch (ServiceException e) {

                    erros.put(
                            "quantidadeItem",
                            e.getMessage()
                    );
                }
            }


            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.getSession().setAttribute(
                        "errosAtualizarProduto",
                        erros
                );

                request.getSession().setAttribute(
                        "idCompraProdutoAtualizar",
                        idCompraProdutoParametro
                );

                request.getSession().setAttribute(
                        "quantidadeItemAtualizar",
                        quantidadeParametro
                );
            }

        } else if ("desvincular".equals(acao)) {

            // ==========================
            // DESVINCULAR PRODUTO
            // ==========================

            String idCompraProdutoParametro =
                    request.getParameter("idCompraProduto");

            try {

                int idCompraProduto = Integer.parseInt(
                        idCompraProdutoParametro
                );

                CompraProduto compraProduto =
                        compraProdutoService.buscarPorId(
                                idCompraProduto
                        );

                if (compraProduto.getCompra().getId() != idCompra) {

                    throw new ServiceException(
                            "O produto não pertence a esta compra"
                    );
                }

                compraProdutoService.deletarPorId(
                        idCompraProduto
                );

            } catch (ServiceException
                     | CompraProdutoNotFoundException
                     | NumberFormatException e) {

                request.getSession().setAttribute(
                        "erroDesvincularProduto",
                        e instanceof NumberFormatException
                                ? "ID do produto da compra inválido"
                                : e.getMessage()
                );

                request.getSession().setAttribute(
                        "idCompraDesvincular",
                        idCompra
                );

                request.getSession().setAttribute(
                        "idCompraProdutoDesvincular",
                        idCompraProdutoParametro
                );
            }

        } else {

            throw new ServletException("Ação inválida");
        }


        response.sendRedirect(
                request.getContextPath() + "/admin/compras"
        );
    }
}
