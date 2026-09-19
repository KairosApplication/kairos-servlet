package com.kairos.model;

import java.time.LocalDate;

import com.kairos.model.enums.TipoUsuario;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

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

//    toString

    @Override
    public String toString() {
        return "-= USUARIO =-" +
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
