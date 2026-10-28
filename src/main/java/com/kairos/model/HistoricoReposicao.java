package com.kairos.model;

public class HistoricoReposicao {

    // Atributos
    private int id;
    private int reposicaoId;
    private String produtoMarca;
    private String produtoNome;
    private int gondolaId;

    // Construtores
    public HistoricoReposicao(int id, int reposicaoId, String produtoMarca, String produtoNome, int gondolaId) {
        this.id = id;
        this.reposicaoId = reposicaoId;
        this.produtoMarca = produtoMarca;
        this.produtoNome = produtoNome;
        this.gondolaId = gondolaId;
    }

    public HistoricoReposicao(int reposicaoId, String produtoMarca, String produtoNome, int gondolaId) {
        this.reposicaoId = reposicaoId;
        this.produtoMarca = produtoMarca;
        this.produtoNome = produtoNome;
        this.gondolaId = gondolaId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getReposicaoId() {
        return reposicaoId;
    }

    public String getProdutoMarca() {
        return produtoMarca;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public int getGondolaId() {
        return gondolaId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setReposicaoId(int reposicaoId) {
        this.reposicaoId = reposicaoId;
    }

    public void setProdutoMarca(String produtoMarca) {
        this.produtoMarca = produtoMarca;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setGondolaId(int gondolaId) {
        this.gondolaId = gondolaId;
    }

    // to String
    @Override
    public String toString() {
        return "\n-= Histórico Reposição =-" +
                "\nID Reposição: " + getReposicaoId() +
                "\nProduto Marca: " + getProdutoMarca() +
                "\nProduto Nome: " + getProdutoNome() +
                "\nID Gôndola: " + getGondolaId();
    }
}
