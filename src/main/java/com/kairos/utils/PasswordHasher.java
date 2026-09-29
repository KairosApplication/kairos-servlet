package com.kairos.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public static String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(10));
    }

    public static boolean verificar(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
}
