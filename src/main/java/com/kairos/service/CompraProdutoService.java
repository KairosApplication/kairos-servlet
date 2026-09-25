package com.kairos.service;

import com.kairos.dao.CompraProdutoDAO;
import com.kairos.model.CompraProduto;
import com.kairos.utils.exceptions.notfound.CompraNotFoundException;
import com.kairos.utils.exceptions.notfound.CompraProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.quantity.PurchaseQuantityException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class CompraProdutoService {

    private final CompraProdutoDAO compraProdutoDAO;
    private final CompraService compraService;
    private final ProdutoService produtoService;

    public CompraProdutoService() {
        this.compraProdutoDAO = new CompraProdutoDAO();
        this.compraService = new CompraService();
        this.produtoService = new ProdutoService();
    }

    public CompraProduto cadastrar(CompraProduto compraProduto) {
        validarCampos(compraProduto);

        if (produtoService.buscarPorId(compraProduto.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (compraService.buscarPorId(compraProduto.getCompra().getId()) == null) {
            throw new CompraNotFoundException("Compra não encontrada");
        }

        return compraProdutoDAO.inserir(compraProduto);
    }

    public CompraProduto buscarPorId(int id) {
        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        CompraProduto compraProduto = compraProdutoDAO.buscarPorId(id);

        if (compraProduto == null) {
            throw new CompraProdutoNotFoundException("Produto da compra não encontrado");
        }

        return compraProduto;
    }

    public List<CompraProduto> listarTodos() {
        return compraProdutoDAO.listarTodos();
    }

    public void atualizar(CompraProduto compraProduto) {
        validarCampos(compraProduto);

        if (compraProduto.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (produtoService.buscarPorId(compraProduto.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (compraService.buscarPorId(compraProduto.getCompra().getId()) == null) {
            throw new CompraNotFoundException("Compra não encontrada");
        }

        if (compraProdutoDAO.atualizar(compraProduto) == 0) {
            throw new CompraProdutoNotFoundException("Produto da compra não encontrado");
        }
    }

    public void deletarPorId(int id) {
        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (compraProdutoDAO.buscarPorId(id) == null) {
            throw new CompraProdutoNotFoundException("Produto da compra não encontrado");
        }

        compraProdutoDAO.deletarPorId(id);
    }

    private void validarCampos(CompraProduto compraProduto) {

        if (compraProduto == null) {
            throw new ServiceException("Produto da compra não pode ser nulo");
        }

        if (compraProduto.getProduto() == null) {
            throw new ServiceException("Produto não pode ser nulo");
        }

        if (compraProduto.getCompra() == null) {
            throw new ServiceException("Compra não pode ser nula");
        }

        if (compraProduto.getQuantidadeItem() < 1) {
            throw new PurchaseQuantityException(
                    "A quantidade do item deve ser maior que 0"
            );
        }
    }
}