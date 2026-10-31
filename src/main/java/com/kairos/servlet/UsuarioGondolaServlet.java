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
        String usuarioId = request.getParameter("idUsuario");
        String gondolaId = request.getParameter("idGondola");

        int idUsuario = 0;
        int idGondola = 0;

        String erroVincularGondola = null;

        // Valida o ID do usuário informado
        if (usuarioId == null || usuarioId.isBlank()) {

            erroVincularGondola = "ID do usuário é obrigatório";

        } else {

            try {

                long idConvertido = Long.parseLong(usuarioId);

                if (idConvertido > Integer.MAX_VALUE) {

                    erroVincularGondola = "ID do usuário não pode ultrapassar 2.147.483.647";

                } else if (idConvertido < 1) {

                    erroVincularGondola = "ID do usuário deve ser maior que zero";

                } else {

                    idUsuario = (int) idConvertido;
                }

            } catch (NumberFormatException e) {
                erroVincularGondola = "ID do usuário deve ser um número inteiro válido";
            }
        }

        // Valida o ID da gôndola informada
        if (erroVincularGondola == null) {

            if (gondolaId == null || gondolaId.isBlank()) {

                erroVincularGondola = "ID da gôndola é obrigatório";

            } else {

                try {

                    long idConvertido = Long.parseLong(gondolaId);

                    if (idConvertido > Integer.MAX_VALUE) {

                        erroVincularGondola = "ID da gôndola não pode ultrapassar 2.147.483.647";

                    } else if (idConvertido < 1) {

                        erroVincularGondola = "ID da gôndola deve ser maior que zero";

                    } else {

                        idGondola = (int) idConvertido;
                    }

                } catch (NumberFormatException e) {
                    erroVincularGondola = "ID da gôndola deve ser um número inteiro válido";
                }
            }
        }

        // Executa a vinculação ou desvinculação quando os IDs são válidos
        if (erroVincularGondola == null) {

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

                erroVincularGondola = e.getMessage();
            }
        }

        // Armazena os erros e IDs na sessão para exibição
        if (erroVincularGondola != null) {

            request.getSession().setAttribute("erroVincularGondola", erroVincularGondola);
            request.getSession().setAttribute("idUsuarioVincular", idUsuario);
            request.getSession().setAttribute("idGondolaVincular", idGondola);
        }

        // Redireciona para a listagem de usuários
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }
}
