package com.kairos.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter DATA_BR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String cpf(String cpf) {

        if (cpf == null) {
            return "";
        }

        return cpf.replaceAll(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                "$1.$2.$3-$4"
        );
    }

    public static String cep(String cep) {

        if (cep == null) {
            return "";
        }

        return cep.replaceAll(
                "(\\d{5})(\\d{3})",
                "$1-$2"
        );
    }

    public static String data(LocalDate data) {

        if (data == null) {
            return "";
        }

        return data.format(DATA_BR);
    }
}