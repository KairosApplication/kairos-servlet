package com.kairos.model;

public class HistoricoReposicao {

    private int id;
    private int reposicaoId;
    private String produtoMarca;
    private String produtoNome;
    private int gondolaId;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReposicaoId() {
        return reposicaoId;
    }

    public void setReposicaoId(int reposicaoId) {
        this.reposicaoId = reposicaoId;
    }

    public String getProdutoMarca() {
        return produtoMarca;
    }

    public void setProdutoMarca(String produtoMarca) {
        this.produtoMarca = produtoMarca;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getGondolaId() {
        return gondolaId;
    }

    public void setGondolaId(int gondolaId) {
        this.gondolaId = gondolaId;
    }

    @Override
    public String toString() {
        return "\n-= Histórico Reposicão =-" +
               "\nID Reposição: " + getReposicaoId() +
               "\nProduto Marca: " + getProdutoMarca() +
               "\nProduto Nome: " + getProdutoNome() +
               "\nID Gôndola: " + getGondolaId();
    }
}