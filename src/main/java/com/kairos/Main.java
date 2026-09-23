package com.kairos;

import com.kairos.dao.CompraDAO;
import com.kairos.dao.ProdutoDAO;
import com.kairos.model.*;
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
            SetorService setorService = new SetorService();
            GondolaService gondolaService = new GondolaService();
            ProdutoDAO produtoDAO = new ProdutoDAO();
            CompraDAO compraDAO = new CompraDAO();

            List<Gondola> gondolas = new ArrayList<>();

//            Setor criandoSetor = new Setor(
//                    "uooouoo",
//                    "godahoia"
//            );
//
//            Setor setor = setorService.cadastrar(criandoSetor);


//
//            Empresa empresa = new Empresa(
//                    "74539825637498",
//                    TipoPlano.STANDART
//            );
//
//            empresaService.cadastrar(empresa);

    //        Usuario usuario = new Usuario(
    //                "73628763894",
    //                "7528ut37ygr",
    //                "victor",
    //                "chandia",
    //                LocalDate.parse("2026-06-07"),
    //                "872639482",
    //                "akmrksm@gmail.com"
    //        );

    //        usuario.setEmpresa(empresaService.buscarPorCnpj("74539825637498"));

    //        usuarioService.cadastrar(usuario);
    //        System.out.println(usuario);

//            Usuario criandoUsuario = new Usuario(
//                    "65123442122",
//                    "abc12642",
//                    "Pedro",
//                    "Gus",
//                    LocalDate.parse("2022-02-04"),
//                    "48721396",
//                    "pedgus@gmail.com"
//            );

//            criandoUsuario.setEmpresa(empresaService.buscarPorCnpj("74539825637498"));
//
//            Usuario usuario = usuarioService.cadastrar(criandoUsuario);
//            System.out.println(usuario);

//            Gondola criandoGondola = new Gondola(
//                    9
//            );
//
//            criandoGondola.setSetor(setor);
//
//            Gondola gondola = gondolaService.cadastrar(criandoGondola);
//            System.out.println(gondola);
//
//            gondolas = gondolaService.listarTodos();
//
//            for (int i = 0; i < gondolas.size(); i++) {
//                System.out.println(gondolas.get(i));
//                System.out.println();
//            }

//            Gondola gondolaNova = new Gondola(
//                    2,
//                    27,
//                    setorService.buscarPorId(1)
//            );
//
//            gondolaService.atualizar(gondolaNova);

//            Produto criandoProduto = new Produto(
//                    "maca",
//                    "meca",
//                    153
//            );
//
//            Produto produto = produtoDAO.inserir(criandoProduto);
//            System.out.println(produto);

//            Compra compra = new Compra(LocalDate.parse("2022-02-05"));

//            compraDAO.inserir(compra);

//            Produto produto = new Produto("Nike", "Tênis Air Max", 15);
//
//            ProdutoService produtoService = new ProdutoService();
////
////            produtoService.cadastrar(produto);
//
//            produtoService.deletarPorId(1);

            Compra compra = new Compra(Timestamp.valueOf("2026-09-22 20:00:00"));

            compraDAO.inserir(compra);


        } catch (DAOException e) {
            System.out.println("erro: " + e.getCause());
        }
    }
}
