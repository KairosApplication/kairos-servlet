package com.kairos.utils.exceptions.exists;

public class NameExistsException extends RuntimeException {

    public NameExistsException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public NameExistsException(String mensagem) {
        super(mensagem);
    }
}
