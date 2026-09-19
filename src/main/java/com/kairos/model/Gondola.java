package com.kairos.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class Gondola {

//    Atributos
    private int id;
    private int capacidadeMaxima;
    private Setor setor;

//    Construtores

    public Gondola(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }


//    toString
    public String toString() {
        return "-= Gondola =- " +
               "\nID: " + getId() +
               "\nCapacidade máxima: " + getCapacidadeMaxima() +
               "\nSetor: " +
               "\n   - ID: " + setor.getId() +
               "\n   - Nome: " + setor.getNome() +
               "\n   - Categoria do setor: " + setor.getCategoria();
    }
}
