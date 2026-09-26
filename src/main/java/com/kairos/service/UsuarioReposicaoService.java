package com.kairos.service;

import com.kairos.dao.UsuarioReposicaoDAO;
import com.kairos.model.Reposicao;
import com.kairos.model.Usuario;
import com.kairos.utils.exceptions.notfound.ReposicaoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class UsuarioReposicaoService {

    private final UsuarioReposicaoDAO usuarioReposicaoDAO;
    private final UsuarioService usuarioService;
    private final ReposicaoService reposicaoService;

    public UsuarioReposicaoService() {
        this.usuarioReposicaoDAO = new UsuarioReposicaoDAO();
        this.usuarioService = new UsuarioService();
        this.reposicaoService = new ReposicaoService();
    }

    public void vincular(Usuario usuario, Reposicao reposicao) {

        validarUsuario(usuario);
        validarReposicao(reposicao);

        usuarioReposicaoDAO.vincular(usuario, reposicao);
    }

    public void desvincular(Usuario usuario, Reposicao reposicao) {

        validarUsuario(usuario);
        validarReposicao(reposicao);

        usuarioReposicaoDAO.desvincular(usuario, reposicao);
    }

    public List<Usuario> listarUsuariosPorReposicao(Reposicao reposicao) {

        validarReposicao(reposicao);

        return usuarioReposicaoDAO.listarUsuariosPorReposicao(reposicao);
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

    private void validarReposicao(Reposicao reposicao) {

        if (reposicao == null) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }

        if (reposicao.getId() <= 0) {
            throw new ServiceException("O id da reposição deve ser maior que 0");
        }

        if (reposicaoService.buscarPorId(reposicao.getId()) == null) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }
    }
}