package com.kairos.service;

import com.kairos.dao.CompraProdutoDAO;
import com.kairos.model.CompraProduto;
import com.kairos.utils.exceptions.notfound.CompraProdutoNotFoundException;
import com.kairos.utils.exceptions.quantity.PurchaseQuantityException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class CompraProdutoService {

    // Atributos
    private final CompraProdutoDAO compraProdutoDAO;
    private final CompraService compraService;
    private final ProdutoService produtoService;

    // Construtor
    public CompraProdutoService() {
        this.compraProdutoDAO = new CompraProdutoDAO();
        this.compraService = new CompraService();
        this.produtoService = new ProdutoService();
    }

    // Cadastra um produto em uma compra
    public CompraProduto cadastrar(CompraProduto compraProduto) {

        validarCampos(compraProduto);

        produtoService.buscarPorId(compraProduto.getProduto().getId());

        compraService.buscarPorId(compraProduto.getCompra().getId());

        if (compraProdutoDAO.existeVinculo(
                compraProduto.getCompra().getId(),
                compraProduto.getProduto().getId())) {

            throw new ServiceException("Este produto já está vinculado a esta compra.");
        }

        return compraProdutoDAO.inserir(compraProduto);
    }

    // Busca um produto da compra pelo ID
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

    // Lista todos os produtos das compras
    public List<CompraProduto> listarTodos() {

        return compraProdutoDAO.listarTodos();
    }

    // Atualiza um produto associado a uma compra
    public void atualizar(CompraProduto compraProduto) {

        if (compraProduto == null) {
            throw new ServiceException("Produto da compra não pode ser nulo");
        }

        if (compraProduto.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (compraProduto.getQuantidadeItem() < 1) {
            throw new PurchaseQuantityException("A quantidade do item deve ser maior que 0");
        }

        if (compraProdutoDAO.atualizar(compraProduto) == 0) {
            throw new CompraProdutoNotFoundException("Produto da compra não encontrado");
        }
    }

    // Exclui um produto associado a uma compra
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (compraProdutoDAO.buscarPorId(id) == null) {
            throw new CompraProdutoNotFoundException("Produto da compra não encontrado");
        }

        compraProdutoDAO.deletarPorId(id);
    }

    // Valida os campos do produto da compra
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
            throw new PurchaseQuantityException("A quantidade do item deve ser maior que 0");
        }
    }
}
