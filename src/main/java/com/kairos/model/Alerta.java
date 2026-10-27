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
    private HistoricoAlerta historicoAlerta;

    public Alerta(int id, Usuario usuario, Gondola gondola, Produto produto,
                  Date dataRuptura, String descricaoAlerta,
                  StatusAlerta statusAlerta, Timestamp dataResolucao) {
        this.id = id;
        this.usuario = usuario;
        this.gondola = gondola;
        this.produto = produto;
        this.dataRuptura = dataRuptura;
        this.descricaoAlerta = descricaoAlerta;
        this.statusAlerta = statusAlerta;
        this.dataResolucao = dataResolucao;
    }

    public Alerta(Usuario usuario, Gondola gondola, Produto produto,
                  Date dataRuptura, String descricaoAlerta,
                  StatusAlerta statusAlerta, Timestamp dataResolucao) {
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

    public HistoricoAlerta getHistoricoAlerta() {
        return historicoAlerta;
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

    public void setHistoricoAlerta(HistoricoAlerta historicoAlerta) {
        this.historicoAlerta = historicoAlerta;
    }

    @Override
    public String toString() {

        String usuarioInfo;

        if (getUsuario() != null) {

            usuarioInfo =
                    "\n   - ID: " + getUsuario().getId() +
                            "\n   - CPF: " + getUsuario().getCpf() +
                            "\n   - Nome: " + getUsuario().getNome() +
                            "\n   - Sobrenome: " + getUsuario().getSobrenome() +
                            "\n   - Data de nascimento: " + getUsuario().getDataNascimento() +
                            "\n   - CEP: " + getUsuario().getCep() +
                            "\n   - Tipo Usuário: " + getUsuario().getTipoUsuario() +
                            "\n   - Email: " + getUsuario().getEmail();

            if (getUsuario().getEmpresa() != null) {

                usuarioInfo +=
                        "\n   - Empresa:" +
                                "\n      - ID: " + getUsuario().getEmpresa().getId() +
                                "\n      - CNPJ: " + getUsuario().getEmpresa().getCnpj() +
                                "\n      - Tipo Plano: " + getUsuario().getEmpresa().getTipoPlano();
            }

        } else if (getHistoricoAlerta() != null) {

            usuarioInfo =
                    "\n   - ID: " + getHistoricoAlerta().getUsuarioId() +
                            "\n   - Usuário excluído";

        } else {

            usuarioInfo = "\n   - Usuário indisponível";
        }


        String gondolaInfo;

        if (getGondola() != null) {

            gondolaInfo =
                    "\n   - ID: " + getGondola().getId() +
                            "\n   - Capacidade máxima: " + getGondola().getCapacidadeMaxima();

            if (getGondola().getSetor() != null) {

                gondolaInfo +=
                        "\n   - Setor:" +
                                "\n      - ID: " + getGondola().getSetor().getId() +
                                "\n      - Nome: " + getGondola().getSetor().getNome() +
                                "\n      - Categoria do setor: " + getGondola().getSetor().getCategoria();
            }

        } else if (getHistoricoAlerta() != null) {

            gondolaInfo =
                    "\n   - ID: " + getHistoricoAlerta().getGondolaId() +
                            "\n   - Gôndola excluída";

        } else {

            gondolaInfo = "\n   - Gôndola indisponível";
        }


        String produtoInfo;

        if (getProduto() != null) {

            produtoInfo =
                    "\n   - ID: " + getProduto().getId() +
                            "\n   - Marca: " + getProduto().getMarca() +
                            "\n   - Nome: " + getProduto().getNome() +
                            "\n   - Quantidade no estoque: " + getProduto().getQuantidadeEstoque();

        } else if (getHistoricoAlerta() != null) {

            produtoInfo =
                    "\n   - ID: " + getHistoricoAlerta().getProdutoId() +
                            "\n   - Produto excluído";

        } else {

            produtoInfo = "\n   - Produto indisponível";
        }


        return "\n-= Alerta =-" +
                "\nID: " + getId() +
                "\nUsuário:" + usuarioInfo +
                "\nGôndola:" + gondolaInfo +
                "\nProduto:" + produtoInfo +
                "\nData da ruptura: " + getDataRuptura() +
                "\nDescrição do alerta: " + getDescricaoAlerta() +
                "\nStatus do alerta: " + getStatusAlerta() +
                "\nData da resolução: " + getDataResolucao();
    }
}
