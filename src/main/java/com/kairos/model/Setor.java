package com.kairos.model;

public class Setor {
    private int id;
    private String nome;
    private String categoria;

//    Construtores

    public Setor(int id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Setor(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    //    toString
    public String toString() {
        return "\n-= Setor =-" +
               "\nID: " + getId() +
               "\nNome: " + getNome() +
               "\nCategoria: " + getCategoria();
    }
}
