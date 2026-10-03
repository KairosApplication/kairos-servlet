package com.kairos.service;

import com.kairos.dao.UsuarioGondolaDAO;
import com.kairos.model.Gondola;
import com.kairos.model.Usuario;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class UsuarioGondolaService {

    private final UsuarioGondolaDAO usuarioGondolaDAO;
    private final UsuarioService usuarioService;
    private final GondolaService gondolaService;

    public UsuarioGondolaService() {
        this.usuarioGondolaDAO = new UsuarioGondolaDAO();
        this.usuarioService = new UsuarioService();
        this.gondolaService = new GondolaService();
    }

    public void vincular(Usuario usuario, Gondola gondola) {

        validarUsuario(usuario);
        validarGondola(gondola);

        if (usuarioGondolaDAO.existeVinculo(usuario, gondola)) {

            throw new ServiceException(
                    "Esta gôndola já está vinculada a este usuário."
            );
        }

        usuarioGondolaDAO.vincular(usuario, gondola);
    }

    public void desvincular(Usuario usuario, Gondola gondola) {

        validarUsuario(usuario);
        validarGondola(gondola);

        usuarioGondolaDAO.desvincular(usuario, gondola);
    }

    public List<Gondola> listarGondolasPorUsuario(Usuario usuario) {

        validarUsuario(usuario);

        return usuarioGondolaDAO.listarGondolasPorUsuario(usuario);
    }

    public List<Usuario> listarUsuariosPorGondola(Gondola gondola) {

        validarGondola(gondola);

        return usuarioGondolaDAO.listarUsuariosPorGondola(gondola);
    }

    private void validarUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        if (usuario.getId() <= 0) {
            throw new ServiceException("O id do usuário deve ser maior que 0");
        }

        if (usuarioService.buscarPorId(usuario.getId()) == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }
    }

    private void validarGondola(Gondola gondola) {

        if (gondola == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (gondola.getId() <= 0) {
            throw new ServiceException("O id da gôndola deve ser maior que 0");
        }

        if (gondolaService.buscarPorId(gondola.getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }
    }

    public boolean existeVinculo(Usuario usuario, Gondola gondola) {

        validarUsuario(usuario);
        validarGondola(gondola);

        return usuarioGondolaDAO.existeVinculo(usuario, gondola);
    }
}