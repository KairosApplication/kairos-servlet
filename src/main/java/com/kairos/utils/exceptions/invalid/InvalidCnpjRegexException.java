package com.kairos.utils.exceptions.invalid;

public class InvalidCnpjRegexException extends RuntimeException {

    public InvalidCnpjRegexException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public InvalidCnpjRegexException(String mensagem) {
        super(mensagem);
    }
}
