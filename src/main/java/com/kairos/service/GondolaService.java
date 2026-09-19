package com.kairos.service;

import com.kairos.dao.GondolaDAO;
import com.kairos.model.Gondola;
import com.kairos.utils.exceptions.GondolaNotFoundException;
import com.kairos.utils.exceptions.MaximumCapacityException;
import com.kairos.utils.exceptions.ServiceException;

import java.util.List;

public class GondolaService {

    private GondolaDAO gondolaDAO;

    public GondolaService() {
        this.gondolaDAO = new GondolaDAO();
    }

    public Gondola cadastrar(Gondola gondola) {

        validarCampos(gondola);

        return gondolaDAO.inserir(gondola);
    }

    public Gondola buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 1");
        }

        Gondola gondola = gondolaDAO.buscarPorId(id);

        if (gondola == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }
        return gondola;
    }

    public List<Gondola> listarTodos() {

        //

        return gondolaDAO.listarTodos();
    }

    public void atualizar(Gondola gondola) {

        validarCampos(gondola);

        if (gondola.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        gondolaDAO.atualizar(gondola);
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Gondola gondola = gondolaDAO.buscarPorId(id);

        if (gondola == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        gondolaDAO.deletarPorId(id);
    }

    private void validarCampos(Gondola gondola) {

        if (gondola == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (gondola.getCapacidadeMaxima() < 1) {
            throw new MaximumCapacityException("Capacidade máxima deve ser maior que 0");
        }
    }




}
