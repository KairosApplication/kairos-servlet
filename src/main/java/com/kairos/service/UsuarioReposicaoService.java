package com.kairos.service;

import com.kairos.dao.HistoricoUsuarioReposicaoDAO;
import com.kairos.dao.UsuarioReposicaoDAO;
import com.kairos.model.Reposicao;
import com.kairos.model.Usuario;
import com.kairos.utils.exceptions.notfound.ReposicaoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class UsuarioReposicaoService {

    private final UsuarioReposicaoDAO usuarioReposicaoDAO;
    private final HistoricoUsuarioReposicaoDAO historicoUsuarioReposicaoDAO;
    private final UsuarioService usuarioService;
    private final ReposicaoService reposicaoService;

    public UsuarioReposicaoService() {
        this.usuarioReposicaoDAO = new UsuarioReposicaoDAO();
        this.historicoUsuarioReposicaoDAO = new HistoricoUsuarioReposicaoDAO();
        this.usuarioService = new UsuarioService();
        this.reposicaoService = new ReposicaoService();
    }

    public void vincular(Usuario usuario, Reposicao reposicao) {

        validarUsuario(usuario);
        validarReposicao(reposicao);

        List<Usuario> usuarios =
                usuarioReposicaoDAO.listarUsuariosPorReposicao(reposicao);

        for (Usuario usuarioVinculado : usuarios) {

            if (usuarioVinculado.getId() == usuario.getId()) {
                throw new ServiceException(
                        "Funcionário já está vinculado a esta reposição"
                );
            }
        }

        usuarioReposicaoDAO.vincular(usuario, reposicao);

        historicoUsuarioReposicaoDAO.salvar(
                reposicao.getId(),
                usuario.getId()
        );
    }

    public void desvincular(int usuarioId, Reposicao reposicao) {

        if (usuarioId <= 0) {
            throw new ServiceException(
                    "O id do usuário deve ser maior que 0"
            );
        }

        validarReposicao(reposicao);

        usuarioReposicaoDAO.desvincular(
                usuarioId,
                reposicao
        );

        historicoUsuarioReposicaoDAO.desativarVinculo(
                reposicao.getId(),
                usuarioId
        );
    }

    public void atualizarDadosUsuario(int usuarioId) {

        if (usuarioId <= 0) {
            throw new ServiceException(
                    "O id do usuário deve ser maior que 0"
            );
        }

        historicoUsuarioReposicaoDAO.atualizarDadosUsuario(
                usuarioId
        );
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
            throw new ServiceException(
                    "O id do usuário deve ser maior que 0"
            );
        }

        if (usuarioService.buscarPorId(usuario.getId()) == null) {
            throw new UsuarioNotFoundException(
                    "Usuário não encontrado"
            );
        }
    }

    private void validarReposicao(Reposicao reposicao) {

        if (reposicao == null) {
            throw new ReposicaoNotFoundException(
                    "Reposição não encontrada"
            );
        }

        if (reposicao.getId() <= 0) {
            throw new ServiceException(
                    "O id da reposição deve ser maior que 0"
            );
        }

        if (reposicaoService.buscarPorId(reposicao.getId()) == null) {
            throw new ReposicaoNotFoundException(
                    "Reposição não encontrada"
            );
        }
    }
}