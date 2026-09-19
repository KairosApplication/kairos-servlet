package com.kairos.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class Produto {

//    Atributos

    private int id;
    private String marca;
    private String nome;
    private int quantidadeEstoque;

//    Construtores

    public Produto(String marca, String nome, int quantidadeEstoque) {
        this.marca = marca;
        this.nome = nome;
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
