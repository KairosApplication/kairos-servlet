package com.kairos.utils;

import com.kairos.model.TipoUsuario;
import com.kairos.model.Usuario;
import com.kairos.utils.exceptions.AdminException;

public class AuthorizationValidator {

    public static void validarAdmin(Usuario usuarioAtual) {
        if (usuarioAtual == null || usuarioAtual.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new AdminException("Apenas o admin pode realizar essa operação");
        }
    }
}
