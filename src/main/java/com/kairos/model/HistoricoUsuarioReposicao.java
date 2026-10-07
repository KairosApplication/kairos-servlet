package com.kairos.model;

public class HistoricoUsuarioReposicao {

    private int id;
    private int reposicaoId;
    private int usuarioId;
    private String usuarioNome;
    private String usuarioSobrenome;
    private String empresaNome;
    private boolean vinculoAtivo;


    public HistoricoUsuarioReposicao(
            int id,
            int reposicaoId,
            int usuarioId,
            String usuarioNome,
            String usuarioSobrenome,
            String empresaNome,
            boolean vinculoAtivo
    ) {
        this.id = id;
        this.reposicaoId = reposicaoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.usuarioSobrenome = usuarioSobrenome;
        this.empresaNome = empresaNome;
        this.vinculoAtivo = vinculoAtivo;
    }


    public HistoricoUsuarioReposicao(
            int reposicaoId,
            int usuarioId,
            String usuarioNome,
            String usuarioSobrenome,
            String empresaNome,
            boolean vinculoAtivo
    ) {
        this.reposicaoId = reposicaoId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.usuarioSobrenome = usuarioSobrenome;
        this.empresaNome = empresaNome;
        this.vinculoAtivo = vinculoAtivo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getReposicaoId() {
        return reposicaoId;
    }

    public void setReposicaoId(int reposicaoId) {
        this.reposicaoId = reposicaoId;
    }


    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioSobrenome() {
        return usuarioSobrenome;
    }

    public void setUsuarioSobrenome(String usuarioSobrenome) {
        this.usuarioSobrenome = usuarioSobrenome;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public boolean isVinculoAtivo() {
        return vinculoAtivo;
    }

    public void setVinculoAtivo(boolean vinculoAtivo) {
        this.vinculoAtivo = vinculoAtivo;
    }
}