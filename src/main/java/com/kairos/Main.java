package com.kairos;

import com.kairos.dao.GondolaDAO;
import com.kairos.model.*;
import com.kairos.service.EmpresaService;
import com.kairos.service.GondolaService;
import com.kairos.service.SetorService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.exceptions.DAOException;

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

            Gondola gondolaNova = new Gondola(
                    2,
                    27,
                    setorService.buscarPorId(1)
            );

            gondolaService.atualizar(gondolaNova);



        } catch (DAOException e) {
            System.out.println("erro: " + e.getCause());
        }
    }
}
