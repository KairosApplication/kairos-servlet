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

    private final UsuarioReposicaoService usuarioReposicaoService = new UsuarioReposicaoService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final ReposicaoService reposicaoService = new ReposicaoService();

    // Processa a vinculação e desvinculação de usuários às reposições
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String usuarioParametro = request.getParameter("idUsuario");
        String reposicaoParametro = request.getParameter("idReposicao");

        int idUsuario = 0;
        int idReposicao = 0;

        try {

            if (!"vincular".equals(acao) && !"desvincular".equals(acao)) {
                throw new ServiceException("Ação inválida");
            }

            if (reposicaoParametro == null || reposicaoParametro.isBlank()) {
                throw new ServiceException("ID da reposição é obrigatório");
            }

            try {
                idReposicao = Integer.parseInt(reposicaoParametro);
            } catch (NumberFormatException e) {
                throw new ServiceException("ID da reposição inválido");
            }

            if (idReposicao <= 0) {
                throw new ServiceException("ID da reposição deve ser maior que 0");
            }

            if (usuarioParametro == null || usuarioParametro.isBlank()) {
                throw new ServiceException("ID do funcionário é obrigatório");
            }

            try {
                idUsuario = Integer.parseInt(usuarioParametro);
            } catch (NumberFormatException e) {
                throw new ServiceException("ID do funcionário inválido");
            }

            if (idUsuario <= 0) {
                throw new ServiceException("ID do funcionário deve ser maior que 0");
            }

            Reposicao reposicao = reposicaoService.buscarPorId(idReposicao);

            if ("vincular".equals(acao)) {

                // Vincula um usuário à reposição
                Usuario usuario = usuarioService.buscarPorId(idUsuario);

                usuarioReposicaoService.vincular(usuario, reposicao);

            } else if ("desvincular".equals(acao)) {

                // Desvincula um usuário da reposição
                usuarioReposicaoService.desvincular(idUsuario, reposicao);
            }

        } catch (ServiceException | ReposicaoNotFoundException | UsuarioNotFoundException e) {

            // Armazena o erro de vinculação na sessão
            request.getSession().setAttribute("erroVincularReposicao", e.getMessage());
            request.getSession().setAttribute("idUsuarioVincular", idUsuario > 0 ? idUsuario : usuarioParametro);
            request.getSession().setAttribute("idReposicaoVincular", idReposicao);
        }

        response.sendRedirect(request.getContextPath() + "/admin/reposicoes");
    }
}
