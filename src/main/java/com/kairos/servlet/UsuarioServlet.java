package com.kairos.servlet;

import com.kairos.model.Usuario;
import com.kairos.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;


@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        Recebendo os parametros da requisição
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        String nome = request.getParameter("nome");
        String sobrenome = request.getParameter("sobrenome");

        LocalDate dataNascimento =
                LocalDate.parse(request.getParameter("dataNascimento"));

        String cep = request.getParameter("cep");
        String email = request.getParameter("email");

//        Usuario usuario = new Usuario(
//                cpf,
//                senha,
//                nome,
//                sobrenome,
//                dataNascimento,
//                cep,
//                email
//        );

        // Precisa das validações e get do usuarioAtual da sessão (validar admin)


    }
}
