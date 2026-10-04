package com.kairos.service;

import com.kairos.dao.ProdutoDAO;
import com.kairos.model.Produto;
import com.kairos.utils.exceptions.exists.NameExistsException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;
import com.kairos.utils.exceptions.quantity.StockQuantityException;

import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public Produto cadastrar(Produto produto) {

        validarCampos(produto);

        if (produtoDAO.existePorNomeEMarca(produto.getNome(), produto.getMarca())) {
            throw new NameExistsException("Produto já cadastrado para esta marca");
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

        return produto;
    }

    public Produto buscarPorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        Produto produto = produtoDAO.buscarPorNome(nome);

        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        return produto;
    }

    public List<Produto> listarTodos() {

        return produtoDAO.listarTodos();
    }

    public Produto buscarPorNomeEMarca(String nome, String marca) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (marca == null || marca.isBlank()) {
            throw new ServiceException("Marca é obrigatória");
        }

        Produto produto = produtoDAO.buscarPorNomeEMarca(nome, marca);

        if (produto == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        return produto;
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

        if (produtoDAO.buscarPorId(id) == null) {
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

    public boolean existePorNomeEMarca(String nome, String marca) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (marca == null || marca.isBlank()) {
            throw new ServiceException("Marca é obrigatória");
        }

        return produtoDAO.existePorNomeEMarca(nome, marca);
    }

    public boolean existePorMarca(String marca) {

        if (marca == null || marca.isBlank()) {
            throw new ServiceException(
                    "Marca é obrigatória"
            );
        }

        return produtoDAO.existePorMarca(marca);
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

    public List<Produto> pesquisar(String pesquisa) {

        return produtoDAO.pesquisar(pesquisa);

    }
}
