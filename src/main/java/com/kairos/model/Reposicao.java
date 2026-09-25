package com.kairos.model;

import java.sql.Timestamp;

public class Reposicao {
    private int id;
    private Usuario usuario;
    private Produto produto;
    private Timestamp dataReposicao;
    private int quantidadeReposto;
    private String motivoReposicao;
    private Gondola gondola;

    public Reposicao(int id, Usuario usuario, Produto produto, Timestamp dataReposicao, int quantidadeReposto, String motivoReposicao, Gondola gondola) {
        this.id = id;
        this.usuario = usuario;
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = motivoReposicao;
        this.gondola = gondola;
    }

    public Reposicao(Usuario usuario, Produto produto, Timestamp dataReposicao, int quantidadeReposto, String motivoReposicao, Gondola gondola) {
        this.usuario = usuario;
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = motivoReposicao;
        this.gondola = gondola;
    }

    public Reposicao(Usuario usuario, Produto produto, Timestamp dataReposicao, int quantidadeReposto, Gondola gondola) {
        this.usuario = usuario;
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = "Falta de Produtos";
        this.gondola = gondola;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Produto getProduto() {
        return produto;
    }

    public Timestamp getDataReposicao() {
        return dataReposicao;
    }

    public int getQuantidadeReposto() {
        return quantidadeReposto;
    }

    public String getMotivoReposicao() {
        return motivoReposicao;
    }

    public Gondola getGondola() {
        return gondola;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setDataReposicao(Timestamp dataReposicao) {
        this.dataReposicao = dataReposicao;
    }

    public void setQuantidadeReposto(int quantidadeReposto) {
        this.quantidadeReposto = quantidadeReposto;
    }

    public void setMotivoReposicao(String motivoReposicao) {
        this.motivoReposicao = motivoReposicao;
    }

    public void setGondola(Gondola gondola) {
        this.gondola = gondola;
    }

    public String toString() {
        return "\n-= Reposição =- " +
               "\nID: " + getId() +
               "\nUsuário: " +
               "\n   ID: " + getUsuario().getId() +
               "\n   CPF: " + getUsuario().getCpf() +
               "\n   Senha: " + getUsuario().getSenha() +
               "\n   Nome: " + getUsuario().getNome() +
               "\n   Sobrenome: " + getUsuario().getSobrenome() +
               "\n   Data de nascimento: " + getUsuario().getDataNascimento() +
               "\n   CEP: " + getUsuario().getCep() +
               "\n   Tipo usuário: " + getUsuario().getTipoUsuario() +
               "\n   Email: " + getUsuario().getEmail() +
               "\n   Empresa:" +
               "\n      ID: " + getUsuario().getEmpresa().getId() +
               "\n      CNPJ" + getUsuario().getEmpresa().getCnpj() +
               "\n      Tipo plano: " + getUsuario().getEmpresa().getTipoPlano() +
               "\nProduto: " +
               "\n   ID: " + getProduto().getId() +
               "\n   Marca: " + getProduto().getMarca() +
               "\n   Nome: " + getProduto().getNome() +
               "\n   Quantidade no estoque: " + getProduto().getQuantidadeEstoque() +
               "\nQuantidade reposto: " + getQuantidadeReposto() +
               "\nMotivo reposição: " + getMotivoReposicao() +
               "\nGôndola:" +
               "\n   ID: " + getGondola().getId() +
               "\n   Capacidade máxima: " + getGondola().getCapacidadeMaxima() +
               "\n   Setor:" +
               "\n      ID: " + getGondola().getSetor().getId() +
               "\n      Nome: " + getGondola().getSetor().getNome() +
               "\n      Categoria setor: " + getGondola().getSetor().getCategoria();
    }
}
