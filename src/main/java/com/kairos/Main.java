package com.kairos;

import com.kairos.dao.UsuarioDAO;
import com.kairos.model.*;
import com.kairos.service.*;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        List<Usuario> usuarios = usuarioDAO.pesquisar("ADMIN");

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

    }
}
