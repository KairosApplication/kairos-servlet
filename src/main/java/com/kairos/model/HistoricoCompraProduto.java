package com.kairos.model;

public class HistoricoCompraProduto {

    //    Atributos

    private int id;
    private int compraProdutoId;
    private int compraId;
    private int produtoId;
    private String produtoMarca;
    private String produtoNome;

    //    Construtores

    public HistoricoCompraProduto(
            int id,
            int compraProdutoId,
            int compraId,
            int produtoId,
            String produtoMarca,
            String produtoNome
    ) {
        this.id = id;
        this.compraProdutoId = compraProdutoId;
        this.compraId = compraId;
        this.produtoId = produtoId;
        this.produtoMarca = produtoMarca;
        this.produtoNome = produtoNome;
    }

    public HistoricoCompraProduto(
            int compraProdutoId,
            int compraId,
            int produtoId,
            String produtoMarca,
            String produtoNome
    ) {
        this.compraProdutoId = compraProdutoId;
        this.compraId = compraId;
        this.produtoId = produtoId;
        this.produtoMarca = produtoMarca;
        this.produtoNome = produtoNome;
    }

    //    Getters

    public int getId() {
        return id;
    }

    public int getCompraProdutoId() {
        return compraProdutoId;
    }

    public int getCompraId() {
        return compraId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public String getProdutoMarca() {
        return produtoMarca;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    //    Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCompraProdutoId(int compraProdutoId) {
        this.compraProdutoId = compraProdutoId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public void setProdutoMarca(String produtoMarca) {
        this.produtoMarca = produtoMarca;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    //    toString

    @Override
    public String toString() {
        return "\n-= Histórico Compra Produto =-" +
                "\nID: " + getId() +
                "\nID da compra produto: " + getCompraProdutoId() +
                "\nID da compra: " + getCompraId() +
                "\nID do produto: " + getProdutoId() +
                "\nMarca do produto: " + getProdutoMarca() +
                "\nNome do produto: " + getProdutoNome();
    }
}
