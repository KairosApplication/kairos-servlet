package com.kairos.service;

import com.kairos.model.Usuario;
import com.kairos.utils.exceptions.system.ServiceException;

public class LoginService {

    private final UsuarioService usuarioService;

    public LoginService() {
        this.usuarioService = new UsuarioService();
    }

    public Usuario autenticar(String email, String senha) {

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (!usuario.getSenha().equals(senha)) {
            throw new ServiceException("Email ou senha inválidos");
        }

        return usuario;
    }
}
