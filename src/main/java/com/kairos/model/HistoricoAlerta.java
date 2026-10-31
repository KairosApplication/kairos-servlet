package com.kairos.model;

public class HistoricoAlerta {

    // Atributos
    private int id;
    private int alertaId;
    private int usuarioId;
    private int gondolaId;
    private int produtoId;

    // Construtores
    public HistoricoAlerta(int id, int alertaId, int usuarioId, int gondolaId, int produtoId) {
        this.id = id;
        this.alertaId = alertaId;
        this.usuarioId = usuarioId;
        this.gondolaId = gondolaId;
        this.produtoId = produtoId;
    }

    public HistoricoAlerta(int alertaId, int usuarioId, int gondolaId, int produtoId) {
        this.alertaId = alertaId;
        this.usuarioId = usuarioId;
        this.gondolaId = gondolaId;
        this.produtoId = produtoId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAlertaId() {
        return alertaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getGondolaId() {
        return gondolaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAlertaId(int alertaId) {
        this.alertaId = alertaId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setGondolaId(int gondolaId) {
        this.gondolaId = gondolaId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    // to String
    @Override
    public String toString() {
        return "\n-= Histórico Alerta =-" +
                "\nID Alerta: " + getAlertaId() +
                "\nID Usuário: " + getUsuarioId() +
                "\nID Gôndola: " + getGondolaId() +
                "\nID Produto: " + getProdutoId();
    }
}
