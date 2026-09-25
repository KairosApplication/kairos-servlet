package com.kairos.model;

import com.kairos.model.enums.StatusAlerta;

import java.sql.Date;
import java.sql.Timestamp;

public class Alerta {

    private int id;
    private Usuario usuario;
    private Gondola gondola;
    private Produto produto;
    private Date dataRuptura;
    private String descricaoAlerta;
    private StatusAlerta statusAlerta;
    private Timestamp dataResolucao;

    public Alerta(int id, Usuario usuario, Gondola gondola, Produto produto, Date dataRuptura, String descricaoAlerta, StatusAlerta statusAlerta, Timestamp dataResolucao) {
        this.id = id;
        this.usuario = usuario;
        this.gondola = gondola;
        this.produto = produto;
        this.dataRuptura = dataRuptura;
        this.descricaoAlerta = descricaoAlerta;
        this.statusAlerta = statusAlerta;
        this.dataResolucao = dataResolucao;
    }

    public Alerta(Usuario usuario, Gondola gondola, Produto produto, Date dataRuptura, String descricaoAlerta, StatusAlerta statusAlerta, Timestamp dataResolucao) {
        this.usuario = usuario;
        this.gondola = gondola;
        this.produto = produto;
        this.dataRuptura = dataRuptura;
        this.descricaoAlerta = descricaoAlerta;
        this.statusAlerta = statusAlerta;
        this.dataResolucao = dataResolucao;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Gondola getGondola() {
        return gondola;
    }

    public Produto getProduto() {
        return produto;
    }

    public Date getDataRuptura() {
        return dataRuptura;
    }

    public String getDescricaoAlerta() {
        return descricaoAlerta;
    }

    public StatusAlerta getStatusAlerta() {
        return statusAlerta;
    }

    public Timestamp getDataResolucao() {
        return dataResolucao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setGondola(Gondola gondola) {
        this.gondola = gondola;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setDataRuptura(Date dataRuptura) {
        this.dataRuptura = dataRuptura;
    }

    public void setDescricaoAlerta(String descricaoAlerta) {
        this.descricaoAlerta = descricaoAlerta;
    }

    public void setStatusAlerta(StatusAlerta statusAlerta) {
        this.statusAlerta = statusAlerta;
    }

    public void setDataResolucao(Timestamp dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    @Override
    public String toString() {
        return "\n-= Alerta =-" +
                "\nID: " + getId() +
                "\nUsuário: " +
                "\n   - ID: " + usuario.getId() +
                "\n   - CPF: " + usuario.getCpf() +
                "\n   - Senha: " + usuario.getSenha() +
                "\n   - Nome: " + usuario.getNome() +
                "\n   - Sobrenome: " + usuario.getSobrenome() +
                "\n   - Data de nascimento: " + usuario.getDataNascimento() +
                "\n   - CEP: " + usuario.getCep() +
                "\n   - Tipo Usuario: " + usuario.getTipoUsuario() +
                "\n   - Email: " + usuario.getEmail() +
                "\n   - Empresa: " +
                "\n      - ID: " + usuario.getEmpresa().getId() +
                "\n      - CNPJ: " + usuario.getEmpresa().getCnpj() +
                "\n      - Tipo Plano: " + usuario.getEmpresa().getTipoPlano() +
                "\nGôndola: " +
                "\n   - ID: " + gondola.getId() +
                "\n   - Capacidade máxima: " + gondola.getCapacidadeMaxima() +
                "\n   - Setor: " +
                "\n      - ID: " + gondola.getSetor().getId() +
                "\n      - Nome: " + gondola.getSetor().getNome() +
                "\n      - Categoria do setor: " + gondola.getSetor().getCategoria() +
                "\nProduto: " +
                "\n   - ID: " + produto.getId() +
                "\n   - Marca: " + produto.getMarca() +
                "\n   - Nome: " + produto.getNome() +
                "\n   - Quantidade no estoque: " + produto.getQuantidadeEstoque() +
                "\nData da ruptura: " + getDataRuptura() +
                "\nDescrição do alerta: " + getDescricaoAlerta() +
                "\nStatus do alerta: " + getStatusAlerta() +
                "\nData da resolução: " + getDataResolucao();
    }
}