package com.kairos.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class Setor {
    private int id;
    private String nome;
    private String categoria;

    public Setor(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

//    toString
    public String toString() {
        return "-= Setor =-" +
               "\nID: " + getId() +
               "\nNome: " + getNome() +
               "\nCategoria: " + getCategoria();
    }
}
