package com.kairos;

import com.kairos.dao.UsuarioDAO;
import com.kairos.model.Empresa;
import com.kairos.model.Setor;
import com.kairos.model.TipoPlano;
import com.kairos.model.Usuario;
import com.kairos.service.EmpresaService;
import com.kairos.service.SetorService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.ConnectionFactory;
import com.kairos.utils.exceptions.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

    try {
        UsuarioService usuarioService = new UsuarioService();
        EmpresaService empresaService= new EmpresaService();
        SetorService setorService = new SetorService();

        Setor setor = new Setor("jakarta", "cacete");

        setorService.cadastrar(setor);

        Setor setor2 = new Setor("j", "manana");

        setorService.cadastrar(setor2);

    } catch (DAOException e) {
        System.out.println("erro: " + e.getCause());
    }



    }
}
