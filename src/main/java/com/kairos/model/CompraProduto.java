package com.kairos.model;

public class CompraProduto {

    private int id;
    private Produto produto;
    private Compra compra;
    private int quantidadeItem;

    public CompraProduto(int id, Produto produto, Compra compra, int quantidadeItem) {
        this.id = id;
        this.produto = produto;
        this.compra = compra;
        this.quantidadeItem = quantidadeItem;
    }

    public CompraProduto(Produto produto, Compra compra, int quantidadeItem) {
        this.produto = produto;
        this.compra = compra;
        this.quantidadeItem = quantidadeItem;
    }

    public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Compra getCompra() {
        return compra;
    }

    public int getQuantidadeItem() {
        return quantidadeItem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public void setQuantidadeItem(int quantidadeItem) {
        this.quantidadeItem = quantidadeItem;
    }

    @Override
    public String toString() {
        return "\n-= Compra Produto =-" +
                "\nID: " + getId() +
                "\nProduto: " +
                "\n   - ID: " + produto.getId() +
                "\n   - Marca: " + produto.getMarca() +
                "\n   - Nome: " + produto.getNome() +
                "\n   - Quantidade no estoque: " + produto.getQuantidadeEstoque() +
                "\nCompra: " +
                "\n   - ID: " + compra.getId() +
                "\n   - Data da compra: " + compra.getDataCompra() +
                "\nQuantidade do item: " + getQuantidadeItem();
    }
}