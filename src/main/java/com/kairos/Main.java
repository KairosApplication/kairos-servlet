package com.kairos;

import com.kairos.dao.CompraDAO;
import com.kairos.dao.ProdutoDAO;
import com.kairos.dao.ReposicaoDAO;
import com.kairos.model.*;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.service.*;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            UsuarioService usuarioService = new UsuarioService();
            EmpresaService empresaService= new EmpresaService();
            ProdutoService produtoService = new ProdutoService();
            SetorService setorService = new SetorService();
            GondolaService gondolaService = new GondolaService();
            ReposicaoService reposicaoService = new ReposicaoService();
            ReposicaoDAO reposicaoDAO = new ReposicaoDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            CompraDAO compraDAO = new CompraDAO();


//            Empresa criarEmpresa = new Empresa(
//                    "76574857463456",
//                    TipoPlano.STANDART
//            );
//
//            Empresa empresa = empresaService.cadastrar(criarEmpresa);
//
//            Usuario criarUsuario = new Usuario(
//                    "87463526473",
//                    "guiv348j34h",
//                    "Victor",
//                    "Chandia",
//                    LocalDate.parse("2020-05-03"),
//                    "85736475",
//                    "akjfka@gmail.com",
//                    empresa
//            );
//
//            Usuario usuario = usuarioService.cadastrar(criarUsuario);
//
//            System.out.println(usuario);

            Usuario usuario = usuarioService.buscarPorId(3);
//
//
            usuario.setNome("atulacatumbatumbata");

            usuarioService.atualizar(usuario);






        } catch (DAOException e) {
            System.out.println("erro: " + e.getCause());
        }
    }
}
