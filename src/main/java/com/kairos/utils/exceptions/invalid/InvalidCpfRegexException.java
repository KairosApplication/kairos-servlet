package com.kairos.utils.exceptions.invalid;

public class InvalidCpfRegexException extends RuntimeException {

    public InvalidCpfRegexException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public InvalidCpfRegexException(String mensagem) {
        super(mensagem);
    }
}
