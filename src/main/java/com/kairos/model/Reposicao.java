package com.kairos.model;

import java.sql.Timestamp;
import java.util.List;

public class Reposicao {

    private int id;
    private Produto produto;
    private Timestamp dataReposicao;
    private int quantidadeReposto;
    private String motivoReposicao;
    private Gondola gondola;
    private HistoricoReposicao historicoReposicao;
    private List<HistoricoUsuarioReposicao> historicoUsuariosReposicoes;

    public Reposicao(int id, Produto produto, Timestamp dataReposicao,
                     int quantidadeReposto, String motivoReposicao, Gondola gondola) {
        this.id = id;
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = motivoReposicao;
        this.gondola = gondola;
    }

    public Reposicao(Produto produto, Timestamp dataReposicao,
                     int quantidadeReposto, String motivoReposicao, Gondola gondola) {
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = motivoReposicao;
        this.gondola = gondola;
    }

    public Reposicao(Produto produto, Timestamp dataReposicao,
                     int quantidadeReposto, Gondola gondola) {
        this.produto = produto;
        this.dataReposicao = dataReposicao;
        this.quantidadeReposto = quantidadeReposto;
        this.motivoReposicao = "Falta de Produtos";
        this.gondola = gondola;
    }

    public int getId() {
        return id;
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

    public HistoricoReposicao getHistoricoReposicao() {
        return historicoReposicao;
    }

    public List<HistoricoUsuarioReposicao> getHistoricoUsuariosReposicoes() {
        return historicoUsuariosReposicoes;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHistoricoReposicao(HistoricoReposicao historicoReposicao) {
        this.historicoReposicao = historicoReposicao;
    }

    public void setHistoricoUsuariosReposicoes(
            List<HistoricoUsuarioReposicao> historicoUsuariosReposicoes) {
        this.historicoUsuariosReposicoes = historicoUsuariosReposicoes;
    }

    @Override
    public String toString() {

        String produto;

        if (getProduto() != null) {
            produto = "\n   ID: " + getProduto().getId() +
                    "\n   Marca: " + getProduto().getMarca() +
                    "\n   Nome: " + getProduto().getNome() +
                    "\n   Quantidade no estoque: " + getProduto().getQuantidadeEstoque();
        } else {
            produto = "\n   Marca: " + getHistoricoReposicao().getProdutoMarca() +
                    "\n   Nome: " + getHistoricoReposicao().getProdutoNome();
        }

        String gondola;

        if (getGondola() != null) {
            gondola = "\n   ID: " + getGondola().getId() +
                    "\n   Capacidade máxima: " + getGondola().getCapacidadeMaxima();

            if (getGondola().getSetor() != null) {
                gondola +=
                        "\n   Setor:" +
                                "\n      ID: " + getGondola().getSetor().getId() +
                                "\n      Nome: " + getGondola().getSetor().getNome() +
                                "\n      Categoria setor: " + getGondola().getSetor().getCategoria();
            }
        } else {
            gondola = "\n   ID: " + getHistoricoReposicao().getGondolaId();
        }

        return "\n-= Reposição =- " +
                "\nID: " + getId() +
                "\nProduto:" +
                produto +
                "\nQuantidade reposto: " + getQuantidadeReposto() +
                "\nMotivo reposição: " + getMotivoReposicao() +
                "\nGôndola:" +
                gondola;
    }
}