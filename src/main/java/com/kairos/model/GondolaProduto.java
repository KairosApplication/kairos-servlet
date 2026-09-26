package com.kairos.model;

public class GondolaProduto {

    private int id;
    private Gondola gondola;
    private Produto produto;

    public GondolaProduto(int id, Gondola gondola, Produto produto) {
        this.id = id;
        this.gondola = gondola;
        this.produto = produto;
    }

    public GondolaProduto(Gondola gondola, Produto produto) {
        this.gondola = gondola;
        this.produto = produto;
    }

    public int getId() {
        return id;
    }

    public Gondola getGondola() {
        return gondola;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGondola(Gondola gondola) {
        this.gondola = gondola;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "\n-= Gondola Produto =-" +
                "\nID: " + getId() +
                "\nGôndola: " +
                "\n   - ID: " + gondola.getId() +
                "\n   - Capacidade máxima: " + gondola.getCapacidadeMaxima() +
                "\n   - Setor: " +
                "\n      - ID: " + gondola.getSetor().getId() +
                "\n      - Nome: " + gondola.getSetor().getNome() +
                "\n      - Categoria do setor: " + gondola.getSetor().getCategoria() +
                "\nProduto: " +
                "\n   - ID: " + produto.getId() +
                "\n   - Marca: " + produto.getMarca() +
                "\n   - Nome: " + produto.getNome() +
                "\n   - Quantidade no estoque: " + produto.getQuantidadeEstoque();
    }
}