package com.kairos.model;

import java.time.LocalDate;

import com.kairos.model.enums.TipoUsuario;

public class Usuario {

//    Atributos

    private int id;
    private String cpf;
    private String senha;
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
    private String cep;
    private TipoUsuario tipoUsuario;
    private String email;
    private Empresa empresa;

//    Construtuores


    public Usuario(int id, String cpf, String senha, String nome, String sobrenome, LocalDate dataNascimento, String cep, TipoUsuario tipoUsuario, String email, Empresa empresa) {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.tipoUsuario = tipoUsuario;
        this.email = email;
        this.empresa = empresa;
    }

    public Usuario(String cpf, String senha, String nome, String sobrenome, LocalDate dataNascimento, String cep, String email, Empresa empresa) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.tipoUsuario = TipoUsuario.FUNCIONARIO;
        this.email = email;
        this.empresa = empresa;
    }

    public Usuario(String cpf, String senha, String nome, String sobrenome, LocalDate dataNascimento, String cep, String email) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.tipoUsuario = TipoUsuario.FUNCIONARIO;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCep() {
        return cep;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getEmail() {
        return email;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    //    toString

    @Override
    public String toString() {
        return "\n-= USUARIO =-" +
               "\nID: " + getId() +
               "\nCPF: " + getCpf() +
               "\nSenha: " + getSenha() +
               "\nNome: " + getNome() +
               "\nSobrenome: " + getSobrenome() +
               "\nData de nascimento: " + getDataNascimento() +
               "\nCEP: " + getCep() +
               "\nTipo Usuario: " + getTipoUsuario() +
               "\nEmail: " + getEmail() +
               "\nEmpresa: " +
               "\n   - ID: " + getEmpresa().getId() +
               "\n   - CNPJ: " + empresa.getCnpj() +
               "\n   - Tipo Plano: " + empresa.getTipoPlano() ;
    }
}
