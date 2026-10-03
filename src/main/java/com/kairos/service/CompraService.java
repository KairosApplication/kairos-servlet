package com.kairos.service;

import com.kairos.dao.CompraDAO;
import com.kairos.model.Compra;
import com.kairos.utils.exceptions.notfound.CompraNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;


public class CompraService {

    private final CompraDAO compraDAO;

    public CompraService() {
        this.compraDAO = new CompraDAO();
    }

    public Compra cadastrar(Compra compra) {

        validarCampos(compra);

        return compraDAO.inserir(compra);
    }

    public Compra buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Compra compra = compraDAO.buscarPorId(id);

        if (compra == null) {
            throw new CompraNotFoundException("Compra não encontrada");
        }
        return compra;
    }

    public List<Compra> listarTodos() {

        return compraDAO.listarTodos();
    }

    public void atualizar(Compra compra) {

        validarCampos(compra);

        if (compra.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        compraDAO.atualizar(compra);
    }

    private void validarCampos(Compra compra) {

        if (compra == null) {
            throw new ServiceException("Compra não pode ser nula");
        }

        if (compra.getDataCompra() == null) {
            throw new ServiceException("A data da compra não pode ser nula");
        }

        if (compra.getDataCompra().after(new Timestamp(System.currentTimeMillis()))) {
            throw new ServiceException("A data da compra não pode ser futura");
        }
    }
}
