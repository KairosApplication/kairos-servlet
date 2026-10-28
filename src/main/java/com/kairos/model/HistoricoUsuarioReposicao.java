package com.kairos.model;

public class HistoricoUsuarioReposicao {

    // Atributos
    private int id;
    private int reposicaoId;
    private int usuarioId;
    private String usuarioNome;
    private String usuarioSobrenome;
    private String empresaNome;
    private boolean vinculoAtivo;

    // Construtores
    public HistoricoUsuarioReposicao(int id, int reposicaoId, int usuarioId, String usuarioNome, String usuarioSobrenome, String empresaNome, boolean vinculoAtivo) {
        this.id = id;
        this.reposicaoId = reposicaoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.usuarioSobrenome = usuarioSobrenome;
        this.empresaNome = empresaNome;
        this.vinculoAtivo = vinculoAtivo;
    }

    public HistoricoUsuarioReposicao(int reposicaoId, int usuarioId, String usuarioNome, String usuarioSobrenome, String empresaNome, boolean vinculoAtivo) {
        this.reposicaoId = reposicaoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.usuarioSobrenome = usuarioSobrenome;
        this.empresaNome = empresaNome;
        this.vinculoAtivo = vinculoAtivo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getReposicaoId() {
        return reposicaoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getUsuarioSobrenome() {
        return usuarioSobrenome;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public boolean isVinculoAtivo() {
        return vinculoAtivo;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setReposicaoId(int reposicaoId) {
        this.reposicaoId = reposicaoId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public void setUsuarioSobrenome(String usuarioSobrenome) {
        this.usuarioSobrenome = usuarioSobrenome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public void setVinculoAtivo(boolean vinculoAtivo) {
        this.vinculoAtivo = vinculoAtivo;
    }

    // to String
    @Override
    public String toString() {
        return "\n-= Histórico Usuário Reposição =-" +
                "\nID: " + getId() +
                "\nID Reposição: " + getReposicaoId() +
                "\nUsuário:" +
                "\n   ID: " + getUsuarioId() +
                "\n   Nome: " + getUsuarioNome() +
                "\n   Sobrenome: " + getUsuarioSobrenome() +
                "\nEmpresa: " + getEmpresaNome() +
                "\nVínculo ativo: " + isVinculoAtivo();
    }
}
