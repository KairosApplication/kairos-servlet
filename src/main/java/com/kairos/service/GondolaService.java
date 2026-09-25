package com.kairos.service;

import com.kairos.dao.GondolaDAO;
import com.kairos.model.Gondola;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.capacity.MaximumCapacityException;
import com.kairos.utils.exceptions.notfound.SetorNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class GondolaService {

    private final GondolaDAO gondolaDAO;
    private final SetorService setorService;

    public GondolaService() {
        this.gondolaDAO = new GondolaDAO();
        this.setorService = new SetorService();
    }

    public Gondola cadastrar(Gondola gondola) {

        validarCampos(gondola);

        if (setorService.buscarPorId(gondola.getSetor().getId()) == null) {
            throw new SetorNotFoundException(
                    "Setor de id " + gondola.getSetor().getId() + " não encontrado"
            );
        }

        return gondolaDAO.inserir(gondola);
    }

    public Gondola buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Gondola gondola = gondolaDAO.buscarPorId(id);

        if (gondola == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        return gondola;
    }

    public List<Gondola> listarTodos() {

        return gondolaDAO.listarTodos();
    }

    public void atualizar(Gondola gondola) {

        validarCampos(gondola);

        if (gondola.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (setorService.buscarPorId(gondola.getSetor().getId()) == null) {
            throw new SetorNotFoundException(
                    "Setor de id " + gondola.getSetor().getId() + " não encontrado"
            );
        }

        if (gondolaDAO.atualizar(gondola) == 0) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (gondolaDAO.buscarPorId(id) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        gondolaDAO.deletarPorId(id);
    }

    private void validarCampos(Gondola gondola) {

        if (gondola == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (gondola.getSetor() == null) {
            throw new ServiceException("Setor é obrigatório");
        }

        if (gondola.getCapacidadeMaxima() < 1) {
            throw new MaximumCapacityException(
                    "Capacidade máxima deve ser maior que 0"
            );
        }
    }
}