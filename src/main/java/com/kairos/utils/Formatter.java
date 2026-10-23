
package com.kairos.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter DATA_BR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Formata o CPF
    public static String cpf(String cpf) {

        if (cpf == null) {
            return "";
        }

        return cpf.replaceAll(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                "$1.$2.$3-$4"
        );
    }

    // Formata o CEP
    public static String cep(String cep) {

        if (cep == null) {
            return "";
        }

        return cep.replaceAll(
                "(\\d{5})(\\d{3})",
                "$1-$2"
        );
    }

    // Formata o CNPJ
    public static String cnpj(String cnpj) {

        if (cnpj == null) {
            return "";
        }

        return cnpj.replaceAll(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5"
        );
    }

    // Formata a data
    public static String data(LocalDate data) {

        if (data == null) {
            return "";
        }

        return data.format(DATA_BR);
    }
}
