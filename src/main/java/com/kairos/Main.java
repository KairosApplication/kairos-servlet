package com.kairos;

import com.kairos.dao.UsuarioDAO;
import com.kairos.model.*;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.service.*;
import com.kairos.model.Usuario;
import com.kairos.utils.PasswordHasher;
import com.kairos.utils.exceptions.system.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        UsuarioService usuarioService = new UsuarioService();


        List<Usuario> usuarios = usuarioService.pesquisar("Kairos");

        for (Usuario usuario: usuarios) {
            System.out.println(usuario);
        }





    }
}
