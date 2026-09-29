package com.kairos.service;

import com.kairos.model.Usuario;
import com.kairos.utils.PasswordHasher;
import com.kairos.utils.exceptions.system.ServiceException;

public class LoginService {

    private final UsuarioService usuarioService;

    public LoginService() {
        this.usuarioService = new UsuarioService();
    }

    public Usuario autenticar(String email, String senha) {

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (!PasswordHasher.verificar(senha, usuario.getSenha())) {
            throw new ServiceException("Email ou senha inválidos");
        }

        return usuario;
    }
}
