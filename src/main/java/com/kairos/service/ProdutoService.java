package com.kairos.service;

import com.kairos.dao.ProdutoDAO;
import com.kairos.model.Produto;
import com.kairos.utils.exceptions.exists.NameExistsException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;
import com.kairos.utils.exceptions.quantity.StockQuantityException;

import java.util.List;

public class ProdutoService {

    // Atributos
    private final ProdutoDAO produtoDAO;

    // Construtor
    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    // Cadastra um novo produto
    public Produto cadastrar(Produto produto) {

        validarCampos(produto);

        if (produtoDAO.existePorNomeEMarca(produto.getNome(), produto.getMarca())) {
            throw new NameExistsException("Produto já cadastrado para esta marca");
        }

        return produtoDAO.inserir(produto);
    }

    // Busca um produto pelo ID
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

    // Busca um produto pelo nome
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

    // Lista todos os produtos
    public List<Produto> listarTodos() {

        return produtoDAO.listarTodos();
    }

    // Busca um produto pelo nome e marca
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

    // Atualiza os dados de um produto
    public void atualizar(Produto produto) {

        validarCampos(produto);

        if (produto.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (produtoDAO.atualizar(produto) == 0) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }
    }

    // Exclui um produto pelo ID
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (produtoDAO.buscarPorId(id) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        produtoDAO.deletarPorId(id);
    }

    // Verifica se existe um produto pelo nome
    public boolean existePorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        return produtoDAO.existePorNome(nome);
    }

    // Verifica se o nome pertence a outro produto
    public boolean existePorNomeExcetoId(String nome, int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        return produtoDAO.existeNomeExcetoId(nome, id);
    }

    // Verifica se existe um produto pelo nome e marca
    public boolean existePorNomeEMarca(String nome, String marca) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (marca == null || marca.isBlank()) {
            throw new ServiceException("Marca é obrigatória");
        }

        return produtoDAO.existePorNomeEMarca(nome, marca);
    }

    // Verifica se existe uma marca cadastrada
    public boolean existePorMarca(String marca) {

        if (marca == null || marca.isBlank()) {
            throw new ServiceException(
                    "Marca é obrigatória"
            );
        }

        return produtoDAO.existePorMarca(marca);
    }

    // Valida os campos do produto
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

    // Pesquisa produtos pelos campos informados
    public List<Produto> pesquisar(String pesquisa) {

        return produtoDAO.pesquisar(pesquisa);
    }
}
