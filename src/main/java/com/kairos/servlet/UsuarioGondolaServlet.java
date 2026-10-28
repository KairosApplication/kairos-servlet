package com.kairos.servlet;

import com.kairos.model.Gondola;
import com.kairos.model.Usuario;
import com.kairos.service.GondolaService;
import com.kairos.service.UsuarioGondolaService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/usuarios-gondolas")
public class UsuarioGondolaServlet extends HttpServlet {

    private final UsuarioGondolaService usuarioGondolaService = new UsuarioGondolaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final GondolaService gondolaService = new GondolaService();

    // Processa a vinculação e desvinculação de usuários e gôndolas
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idGondola = Integer.parseInt(request.getParameter("idGondola"));

        try {

            // Busca o usuário e a gôndola pelos IDs informados
            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Gondola gondola = gondolaService.buscarPorId(idGondola);

            // Executa a ação solicitada
            if ("vincular".equals(acao)) {

                usuarioGondolaService.vincular(usuario, gondola);

            } else if ("desvincular".equals(acao)) {

                usuarioGondolaService.desvincular(usuario, gondola);

            } else {

                throw new ServletException("Ação inválida");
            }

        } catch (ServiceException | GondolaNotFoundException | UsuarioNotFoundException e) {

            // Armazena os erros e IDs na sessão para exibição
            request.getSession().setAttribute("erroVincularGondola", e.getMessage());
            request.getSession().setAttribute("idUsuarioVincular", idUsuario);
            request.getSession().setAttribute("idGondolaVincular", idGondola);
        }

        // Redireciona para a listagem de usuários
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }
}
