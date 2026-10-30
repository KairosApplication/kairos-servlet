package com.kairos.model;

public class Admin {

    // Atributos
    private int id;
    private String email;
    private String senha;

    // Construtores
    public Admin(int id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }

    public Admin(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
