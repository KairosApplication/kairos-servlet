package com.kairos.model;

import com.kairos.model.enums.TipoPlano;

public class Empresa {

    // Atributos

    private int id;
    private String nome;
    private String cnpj;
    private TipoPlano tipoPlano;

    // Construtores

    public Empresa(int id, String nome, String cnpj, TipoPlano tipoPlano) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipoPlano = tipoPlano;
    }

    public Empresa(String nome, String cnpj, TipoPlano tipoPlano) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipoPlano = tipoPlano;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public TipoPlano getTipoPlano() {
        return tipoPlano;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setTipoPlano(TipoPlano tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    // toString

    @Override
    public String toString() {
        return "\n-= Empresa =-" +
                "\nID: " + getId() +
                "\nNome: " + getNome() +
                "\nCNPJ: " + getCnpj() +
                "\nTipo do plano: " + getTipoPlano();
    }
}