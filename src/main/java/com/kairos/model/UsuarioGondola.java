package com.kairos.model;

public class UsuarioGondola {

    private int id;
    private Gondola gondola;
    private Usuario usuario;

    public UsuarioGondola(int id, Gondola gondola, Usuario usuario) {
        this.id = id;
        this.gondola = gondola;
        this.usuario = usuario;
    }

    public UsuarioGondola(Gondola gondola, Usuario usuario) {
        this.gondola = gondola;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public Gondola getGondola() {
        return gondola;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGondola(Gondola gondola) {
        this.gondola = gondola;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "\n-= Usuario Gôndola =-" +
                "\nID: " + getId() +
                "\nGôndola: " +
                "\n   - ID: " + gondola.getId() +
                "\n   - Capacidade máxima: " + gondola.getCapacidadeMaxima() +
                "\n   - Setor: " +
                "\n      - ID: " + gondola.getSetor().getId() +
                "\n      - Nome: " + gondola.getSetor().getNome() +
                "\n      - Categoria do setor: " + gondola.getSetor().getCategoria() +
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
                "\n      - Tipo Plano: " + usuario.getEmpresa().getTipoPlano();
    }
}