package com.kairos.service;

import com.kairos.dao.SetorDAO;
import com.kairos.model.Setor;
import com.kairos.utils.exceptions.NameExistsException;
import com.kairos.utils.exceptions.ServiceException;
import com.kairos.utils.exceptions.SetorNotFoundException;

public class SetorService {

    private final SetorDAO setorDAO;

    public SetorService() {
        this.setorDAO = new SetorDAO();
    }

    public void cadastrar(Setor setor) {

        validarCampos(setor);

        if (setorDAO.existePorNome(setor.getNome())) {
            throw new NameExistsException("Nome de setor já cadastrado");
        }

        setorDAO.inserir(setor);

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
