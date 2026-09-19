package com.kairos.utils.exceptions;

public class GondolaNotFoundException extends RuntimeException {

    public GondolaNotFoundException(String mensagem) {
        super(mensagem);
    }

    public GondolaNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
