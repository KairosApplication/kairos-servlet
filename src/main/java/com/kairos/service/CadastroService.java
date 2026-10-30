package com.kairos.service;

import com.kairos.utils.Regex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadastroService {

    // Valida a primeira etapa do cadastro
    public List<String> validarCadastro1(String nome, String email, String senha) {

        List<String> erros = new ArrayList<>();

        // Valida o nome
        if (nome == null || nome.isBlank()) {

            erros.add("Nome obrigatório");

        } else if (!nome.matches("[a-zA-ZÀ-ÿ ]+")) {

            erros.add("O nome deve conter apenas letras");
        }

        // Valida o email
        if (email == null || email.isBlank()) {

            erros.add("E-mail obrigatório");

        } else if (!email.matches(Regex.EMAIL)) {

            erros.add("E-mail inválido");
        }

        // Valida a senha
        if (senha == null || senha.isBlank()) {

            erros.add("Senha obrigatória");

        } else if (senha.length() < 8) {

            erros.add("A senha deve conter pelo menos 8 caracteres");
        }

        return erros;
    }

    // Valida a segunda etapa do cadastro
    public List<String> validarCadastro2(LocalDate dataNascimento, String cpf, String cep) {

        List<String> erros = new ArrayList<>();

        LocalDate dataLimite = LocalDate.of(1900, 1, 1);

        // Valida a data de nascimento
        if (dataNascimento == null) {

            erros.add("Data de nascimento obrigatória");

        } else if (dataNascimento.isAfter(LocalDate.now())) {

            erros.add("Data de nascimento inválida");

        } else if (dataNascimento.isBefore(dataLimite)) {

            erros.add("A data de nascimento deve ser a partir de 1900");
        }

        // Valida o CPF
        if (cpf == null || cpf.isBlank()) {

            erros.add("CPF obrigatório");

        } else if (!cpf.matches(Regex.CPF)) {

            erros.add("CPF inválido");
        }

        // Valida o CEP
        if (cep == null || cep.isBlank()) {

            erros.add("CEP obrigatório");

        } else if (!cep.matches(Regex.CEP)) {

            erros.add("CEP inválido");
        }

        return erros;
    }
}
