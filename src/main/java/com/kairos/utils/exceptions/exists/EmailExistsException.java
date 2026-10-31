package com.kairos.utils.exceptions.exists;

// Exceção para email já existente
public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String mensagem) {
        super(mensagem);
    }

    public EmailExistsException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
