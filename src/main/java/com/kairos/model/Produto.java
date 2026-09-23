package com.kairos.model;

public class Produto {

//    Atributos

    private int id;
    private String marca;
    private String nome;
    private int quantidadeEstoque;

//    Construtores

    public Produto(int id, String marca, String nome, int quantidadeEstoque) {
        this.id = id;
        this.marca = marca;
        this.nome = nome;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(String marca, String nome, int quantidadeEstoque) {
        this.marca = marca;
        this.nome = nome;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    //    toString
    @Override
    public String toString() {
        return "-= Produto =- " +
               "\nID: " + getId() +
               "\nMarca: " + getMarca() +
               "\nNome: " + getNome() +
               "\nQuantidade no estoque: " + getQuantidadeEstoque();
    }
}
