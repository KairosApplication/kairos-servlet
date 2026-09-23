package com.kairos.service;

import com.kairos.dao.SetorDAO;
import com.kairos.model.Setor;
import com.kairos.utils.exceptions.exists.NameExistsException;
import com.kairos.utils.exceptions.system.ServiceException;
import com.kairos.utils.exceptions.notfound.SetorNotFoundException;

import java.util.List;

public class SetorService {

    private final SetorDAO setorDAO;

    public SetorService() {
        this.setorDAO = new SetorDAO();
    }

    public Setor cadastrar(Setor setor) {

        validarCampos(setor);

        if (setorDAO.existePorNome(setor.getNome())) {
            throw new NameExistsException("Nome de setor já cadastrado");
        }
        return setorDAO.inserir(setor);
    }

    public Setor buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Setor setor = setorDAO.buscarPorId(id);

        if (setor == null) {
            throw new SetorNotFoundException("Setor não encontrado");
        }
        return setor;
    }

    public List<Setor> listarTodos() {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        return setorDAO.listarTodos();
    }

    public void atualizar(Setor setor) {

        validarCampos(setor);

        if (setor.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (setorDAO.existePorNomeExcetoId(setor.getNome(), setor.getId())) {
            throw new NameExistsException("Nome já cadastrado");
        }

        if (setorDAO.atualizar(setor) == 0) {
            throw new SetorNotFoundException("Setor não encontrado");
        }
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (setorDAO.buscarPorId(id) == null) {
            throw new SetorNotFoundException("Setor não encontrado");
        }

        setorDAO.deletarPorId(id);
    }

    public boolean existePorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrgatório");
        }

        return setorDAO.existePorNome(nome);
    }

    public boolean existePorNomeExcetoId(String nome, int id) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        return setorDAO.existePorNomeExcetoId(nome, id);
    }

    private void validarCampos(Setor setor) {

        if (setor == null) {
            throw new ServiceException("Setor não pode ser nulo");
        }

        if (setor.getNome() == null || setor.getNome().isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (setor.getCategoria() == null || setor.getCategoria().isBlank()) {
            throw new ServiceException("Categoria é obrigatória");
        }
    }
}
