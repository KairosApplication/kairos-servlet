package com.kairos.utils.exceptions.regex;

public class InvalidCnpjRegexException extends RuntimeException {

    public InvalidCnpjRegexException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public InvalidCnpjRegexException(String mensagem) {
        super(mensagem);
    }
}
