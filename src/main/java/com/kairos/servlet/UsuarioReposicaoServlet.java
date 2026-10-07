package com.kairos.servlet;

import com.kairos.model.Reposicao;
import com.kairos.model.Usuario;
import com.kairos.service.ReposicaoService;
import com.kairos.service.UsuarioReposicaoService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.exceptions.notfound.ReposicaoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/usuarios-reposicoes")
public class UsuarioReposicaoServlet extends HttpServlet {

    private final UsuarioReposicaoService usuarioReposicaoService =
            new UsuarioReposicaoService();

    private final UsuarioService usuarioService =
            new UsuarioService();

    private final ReposicaoService reposicaoService =
            new ReposicaoService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        int idUsuario = Integer.parseInt(
                request.getParameter("idUsuario")
        );

        int idReposicao = Integer.parseInt(
                request.getParameter("idReposicao")
        );

        try {

            Reposicao reposicao =
                    reposicaoService.buscarPorId(idReposicao);

            if ("vincular".equals(acao)) {

                Usuario usuario =
                        usuarioService.buscarPorId(idUsuario);

                usuarioReposicaoService.vincular(
                        usuario,
                        reposicao
                );

            } else if ("desvincular".equals(acao)) {

                usuarioReposicaoService.desvincular(
                        idUsuario,
                        reposicao
                );

            } else {

                throw new ServletException("Ação inválida");
            }

        } catch (ServiceException
                 | ReposicaoNotFoundException
                 | UsuarioNotFoundException e) {

            request.getSession().setAttribute(
                    "erroVincularReposicao",
                    e.getMessage()
            );

            request.getSession().setAttribute(
                    "idUsuarioVincular",
                    idUsuario
            );

            request.getSession().setAttribute(
                    "idReposicaoVincular",
                    idReposicao
            );
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/reposicoes"
        );
    }
}