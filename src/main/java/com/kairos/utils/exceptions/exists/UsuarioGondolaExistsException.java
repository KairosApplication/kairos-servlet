package com.kairos.utils.exceptions.exists;

public class UsuarioGondolaExistsException extends RuntimeException {

    public UsuarioGondolaExistsException(String message, Throwable problema) {
        super(message, problema);
    }

    public UsuarioGondolaExistsException(String message) {
        super(message);
    }
}
