package com.kairos.model;

public class Gondola {

//    Atributos
    private int id;
    private int capacidadeMaxima;
    private Setor setor;

//    Construtores

    public Gondola(int id, int capacidadeMaxima, Setor setor) {
        this.id = id;
        this.capacidadeMaxima = capacidadeMaxima;
        this.setor = setor;
    }

    public Gondola(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getId() {
        return id;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
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
