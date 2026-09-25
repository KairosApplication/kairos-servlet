package com.kairos.model;

import com.kairos.model.enums.TipoPlano;

public class Empresa {

//    Atributos

    private int id;
    private String cnpj;
    private TipoPlano tipoPlano;

//    Construtores

    public Empresa(int id, String cnpj, TipoPlano tipoPlano) {
        this.id = id;
        this.cnpj = cnpj;
        this.tipoPlano = tipoPlano;
    }

    public Empresa(String cnpj, TipoPlano tipoPlano) {
        this.cnpj = cnpj;
        this.tipoPlano = tipoPlano;
    }

    public int getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public TipoPlano getTipoPlano() {
        return tipoPlano;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setTipoPlano(TipoPlano tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    //    toString
    @Override
    public String toString() {
        return "\n-= Empresa =-" +
               "\nID: " + getId() +
               "\nCNPJ: " + getCnpj() +
               "\nTipo do plano: " + getTipoPlano();
    }
}
