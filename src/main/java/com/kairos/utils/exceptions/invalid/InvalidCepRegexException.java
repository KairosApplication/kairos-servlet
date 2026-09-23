package com.kairos.utils.exceptions.invalid;

public class InvalidCepRegexException extends RuntimeException {

    public InvalidCepRegexException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public InvalidCepRegexException(String mensagem) {
        super(mensagem);
    }
}
