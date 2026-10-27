package com.kairos.utils;

import java.security.SecureRandom;

public class PasswordGenerator {

    // Caracteres permitidos na senha
    private static final String CARACTERES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    // Gerador de números aleatórios seguros
    private static final SecureRandom RANDOM = new SecureRandom();

    // Gera uma senha aleatória
    public static String gerar(int tamanho) {

        StringBuilder senha = new StringBuilder(tamanho);

        for (int i = 0; i < tamanho; i++) {

            int indice = RANDOM.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(indice));

        }

        return senha.toString();
    }
}
