package com.kairos.service;

import com.kairos.dao.CompraDAO;
import com.kairos.model.Compra;
import com.kairos.utils.exceptions.notfound.CompraNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;

public class CompraService {

    // Atributos
    private final CompraDAO compraDAO;

    // Construtor
    public CompraService() {
        this.compraDAO = new CompraDAO();
    }

    // Cadastra uma nova compra
    public Compra cadastrar(Compra compra) {

        validarCampos(compra);

        return compraDAO.inserir(compra);
    }

    // Busca uma compra pelo ID
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

    // Lista todas as compras
    public List<Compra> listarTodos() {

        return compraDAO.listarTodos();
    }

    // Pesquisa compras
    public List<Compra> pesquisar(String busca) {

        return compraDAO.pesquisar(busca);
    }

    // Atualiza uma compra existente
    public void atualizar(Compra compra) {

        validarCampos(compra);

        if (compra.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        compraDAO.atualizar(compra);
    }

    // Exclui uma compra pelo ID
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (compraDAO.buscarPorId(id) == null) {
            throw new CompraNotFoundException("Compra não encontrada");
        }

        compraDAO.deletarPorId(id);
    }

    // Valida os campos da compra
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
