package com.kairos.model;

public class UsuarioReposicao {

    // Atributos

    private int id;
    private Reposicao reposicao;
    private Usuario usuario;

    // Construtores

    public UsuarioReposicao(int id, Reposicao reposicao, Usuario usuario) {
        this.id = id;
        this.reposicao = reposicao;
        this.usuario = usuario;
    }

    public UsuarioReposicao(Reposicao reposicao, Usuario usuario) {
        this.reposicao = reposicao;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public Reposicao getReposicao() {
        return reposicao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReposicao(Reposicao reposicao) {
        this.reposicao = reposicao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // toString

    @Override
    public String toString() {
        return "\n-= Usuário Reposição =-" +
                "\nID: " + getId() +

                "\nReposição: " +
                "\n   - ID: " + getReposicao().getId() +
                "\n   - Produto: " +
                "\n      - ID: " + getReposicao().getProduto().getId() +
                "\n      - Marca: " + getReposicao().getProduto().getMarca() +
                "\n      - Nome: " + getReposicao().getProduto().getNome() +
                "\n      - Quantidade no estoque: " + getReposicao().getProduto().getQuantidadeEstoque() +
                "\n   - Data da reposição: " + getReposicao().getDataReposicao() +
                "\n   - Quantidade reposta: " + getReposicao().getQuantidadeReposto() +
                "\n   - Motivo da reposição: " + getReposicao().getMotivoReposicao() +
                "\n   - Gôndola: " +
                "\n      - ID: " + getReposicao().getGondola().getId() +
                "\n      - Capacidade máxima: " + getReposicao().getGondola().getCapacidadeMaxima() +
                "\n      - Setor: " +
                "\n         - ID: " + getReposicao().getGondola().getSetor().getId() +
                "\n         - Nome: " + getReposicao().getGondola().getSetor().getNome() +
                "\n         - Categoria do setor: " + getReposicao().getGondola().getSetor().getCategoria() +

                "\nUsuário: " +
                "\n   - ID: " + getUsuario().getId() +
                "\n   - CPF: " + getUsuario().getCpf() +
                "\n   - Senha: " + getUsuario().getSenha() +
                "\n   - Nome: " + getUsuario().getNome() +
                "\n   - Sobrenome: " + getUsuario().getSobrenome() +
                "\n   - Data de nascimento: " + getUsuario().getDataNascimento() +
                "\n   - CEP: " + getUsuario().getCep() +
                "\n   - Tipo usuário: " + getUsuario().getTipoUsuario() +
                "\n   - Email: " + getUsuario().getEmail() +
                "\n   - Empresa: " +
                "\n      - ID: " + getUsuario().getEmpresa().getId() +
                "\n      - CNPJ: " + getUsuario().getEmpresa().getCnpj() +
                "\n      - Tipo plano: " + getUsuario().getEmpresa().getTipoPlano();
    }
}