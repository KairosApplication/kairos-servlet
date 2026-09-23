package com.kairos.service;

import com.kairos.dao.ProdutoDAO;
import com.kairos.model.Produto;
import com.kairos.utils.exceptions.exists.NameExistsException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;
import com.kairos.utils.exceptions.quantity.StockQuantityException;

import java.util.List;

public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public Produto cadastrar(Produto produto) {

        validarCampos(produto);

        if (produtoDAO.existePorNome(produto.getNome())) {
            throw new NameExistsException("Nome já cadastrado");
        }

        return produtoDAO.inserir(produto);
    }

    public Produto buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Produto produto = produtoDAO.buscarPorId(id);

        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        return produtoDAO.inserir(produto);
    }

    public List<Produto> listarTodos() {

//

        return produtoDAO.listarTodos();
    }

    public void atualizar(Produto produto) {

        validarCampos(produto);

        if (produto.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (produtoDAO.atualizar(produto) == 0) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Produto produto = produtoDAO.buscarPorId(id);

        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        produtoDAO.deletarPorId(id);
    }

    public boolean existePorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        return produtoDAO.existePorNome(nome);
    }

    public boolean existePorNomeExcetoId(String nome, int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        return produtoDAO.existeNomeExcetoId(nome, id);
    }

    private void validarCampos(Produto produto) {

        if (produto == null) {
            throw new ServiceException("Produto não pode ser nulo");
        }

        if (produto.getMarca() == null || produto.getMarca().isBlank()) {
            throw new ServiceException("Marca é obrigatório");
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (produto.getQuantidadeEstoque() < 0) {
            throw new StockQuantityException("A quantidade no estoque não pode ser menor que 0");
        }
    }
}
