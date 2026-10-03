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

@WebServlet("/admin/gondolas-produtos")
public class GondolaProdutoServlet extends HttpServlet {

    private final GondolaProdutoService gondolaProdutoService =
            new GondolaProdutoService();

    private final GondolaService gondolaService =
            new GondolaService();

    private final ProdutoService produtoService =
            new ProdutoService();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao =
                request.getParameter("acao");

        int idGondola =
                Integer.parseInt(
                        request.getParameter("idGondola")
                );

        String nomeProduto =
                request.getParameter("nomeProduto");


        try {

            Gondola gondola =
                    gondolaService.buscarPorId(idGondola);

            Produto produto =
                    produtoService.buscarPorNome(
                            nomeProduto
                    );

            GondolaProduto gondolaProduto =
                    new GondolaProduto(
                            gondola,
                            produto
                    );


            // ==========================
            // VINCULAR
            // ==========================

            if ("vincular".equals(acao)) {

                gondolaProdutoService.vincular(
                        gondolaProduto
                );


                // ==========================
                // DESVINCULAR
                // ==========================

            } else if ("desvincular".equals(acao)) {

                gondolaProdutoService.desvincular(
                        gondolaProduto
                );


                // ==========================
                // AÇÃO INVÁLIDA
                // ==========================

            } else {

                throw new ServletException(
                        "Ação inválida"
                );
            }


        } catch (
                ServiceException
                | GondolaNotFoundException
                | ProdutoNotFoundException e
        ) {

            request.getSession().setAttribute(
                    "erroVincularProduto",
                    e.getMessage()
            );

            request.getSession().setAttribute(
                    "idGondolaVincularProduto",
                    idGondola
            );

            request.getSession().setAttribute(
                    "nomeProdutoVincular",
                    nomeProduto
            );
        }


        // ==========================
        // REDIRECT
        // ==========================

        response.sendRedirect(
                request.getContextPath() + "/admin/gondolas"
        );
    }
}