
package com.kairos.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Gera o hash da senha
    public static String hash(String senha) {

        return BCrypt.hashpw(senha, BCrypt.gensalt(10));
    }

    // Verifica se a senha corresponde ao hash
    public static boolean verificar(String senha, String hash) {

        return BCrypt.checkpw(senha, hash);
    }
}
