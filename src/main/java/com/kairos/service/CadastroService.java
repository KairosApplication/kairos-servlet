package com.kairos.service;

import com.kairos.utils.Regex;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadastroService {

    public List<String> validarCadastro1(String nome, String email, String senha) {

        List<String> erros = new ArrayList<>();

        if (nome == null || nome.isBlank()) {
            erros.add("Nome obrigatório");
        } else if (!nome.matches("[a-zA-ZÀ-ÿ ]+")) {
            erros.add("O nome deve conter apenas letras");
        }

        if (email == null || email.isBlank()) {
            erros.add("E-mail obrigatório");
        } else if (!email.matches(Regex.EMAIL)) {
            erros.add("E-mail inválido");
        }

        if (senha == null || senha.isBlank()) {
            erros.add("Senha obrigatória");
        } else if (senha.length() < 8) {
            erros.add("A senha deve conter pelo menos 8 caracteres");

        }

        return erros;
    }

    public List<String> validarCadastro2(LocalDate dataNascimento, String cpf, String cep) {

        List<String> erros = new ArrayList<>();

        LocalDate dataLimite = LocalDate.of(1900, 1, 1);

        if (dataNascimento == null) {
            erros.add("Data de nascimento obrigatória");
        } else if (dataNascimento.isAfter(LocalDate.now())) {
            erros.add("Data de nascimento inválida");
        } else if (dataNascimento.isBefore(dataLimite)) {
            erros.add("A data de nascimento deve ser a partir de 1900");
        }

        if (cpf == null || cpf.isBlank()) {
            erros.add("CPF obrigatório");
        } else if (!cpf.matches(Regex.CPF)) {
            erros.add("CPF inválido");
        }

        if (cep == null || cep.isBlank()) {
            erros.add("CEP obrigatório");
        } else if (!cep.matches(Regex.CEP)) {
            erros.add("CEP inválido");
        }

        return erros;
    }
}
