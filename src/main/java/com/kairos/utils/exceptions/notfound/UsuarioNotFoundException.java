package com.kairos.utils.exceptions.notfound;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public UsuarioNotFoundException(String mensagem) {
        super(mensagem);
    }
}
