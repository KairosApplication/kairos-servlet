package com.kairos.model;

public class CompraProduto {

    // Atributos
    private int id;
    private Produto produto;
    private Compra compra;
    private int quantidadeItem;
    private HistoricoCompraProduto historicoCompraProduto;

    // Construtores
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

    // Getters
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

    public HistoricoCompraProduto getHistoricoCompraProduto() {
        return historicoCompraProduto;
    }

    // Setters
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

    public void setHistoricoCompraProduto(HistoricoCompraProduto historicoCompraProduto) {
        this.historicoCompraProduto = historicoCompraProduto;
    }

    // to String
    @Override
    public String toString() {

        String produto;

        if (getProduto() != null) {

            produto = "\n   - ID: " + getProduto().getId() +
                    "\n   - Marca: " + getProduto().getMarca() +
                    "\n   - Nome: " + getProduto().getNome() +
                    "\n   - Quantidade no estoque: " + getProduto().getQuantidadeEstoque();

        } else if (getHistoricoCompraProduto() != null) {

            produto = "\n   - ID: " + getHistoricoCompraProduto().getProdutoId() +
                    "\n   - Marca: " + getHistoricoCompraProduto().getProdutoMarca() +
                    "\n   - Nome: " + getHistoricoCompraProduto().getProdutoNome();

        } else {

            produto = "\n   - Produto não encontrado";
        }

        return "\n-= Compra Produto =-" +
                "\nID: " + getId() +
                "\nProduto: " +
                produto +
                "\nCompra: " +
                "\n   - ID: " + compra.getId() +
                "\n   - Data da compra: " + compra.getDataCompra() +
                "\nQuantidade do item: " + getQuantidadeItem();
    }
}
