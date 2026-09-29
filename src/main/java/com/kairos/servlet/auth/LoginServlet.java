package com.kairos.servlet.auth;

import com.kairos.model.Usuario;
import com.kairos.service.LoginService;
import com.kairos.utils.exceptions.regex.InvalidEmailRegexException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final LoginService loginService;

    public LoginServlet() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {

            Usuario usuario = loginService.autenticar(email, senha);

            response.getWriter().println("Login realizado com sucesso!");

        } catch (ServiceException e) {

            request.setAttribute("erroService", e.getMessage());

            request.getRequestDispatcher("/WEB-INF/views/login/login.jsp")
                    .forward(request, response);
        }

        catch (UsuarioNotFoundException | InvalidEmailRegexException e) {

            request.setAttribute("erroEmailOuSenha", "E-mail ou senha inválidos");

            request.getRequestDispatcher("/WEB-INF/views/login/login.jsp")
                    .forward(request, response);
        }
    }
}
