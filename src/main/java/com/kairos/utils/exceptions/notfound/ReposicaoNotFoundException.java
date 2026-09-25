package com.kairos.utils.exceptions.notfound;

public class ReposicaoNotFoundException extends RuntimeException {

    public ReposicaoNotFoundException(String message, Throwable problema) {
        super(message, problema);
    }

    public ReposicaoNotFoundException(String message) {
        super(message);
    }
}
