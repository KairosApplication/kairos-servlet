package com.kairos.utils.exceptions;

public class InvalidEmailRegexException extends RuntimeException {

    public InvalidEmailRegexException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public InvalidEmailRegexException(String mensagem) {
        super(mensagem);
    }
}
