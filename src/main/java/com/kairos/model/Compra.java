package com.kairos.model;

import java.sql.Timestamp;

public class Compra {

//    Atributos

    private int id;
    private Timestamp dataCompra;

//    Construtores

    public Compra(int id, Timestamp dataCompra) {
        this.id = id;
        this.dataCompra = dataCompra;
    }

    public Compra(Timestamp dataCompra) {
        this.dataCompra = dataCompra;
    }

    public int getId() {
        return id;
    }

    public Timestamp getDataCompra() {
        return dataCompra;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataCompra(Timestamp dataCompra) {
        this.dataCompra = dataCompra;
    }

    //    toString
    @Override
    public String toString() {
        return "-= Compra =- " +
               "\nID: " + getId() +
               "\nData da compra: " + getDataCompra();
    }
}
