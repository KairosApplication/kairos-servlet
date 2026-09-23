package com.kairos.utils.exceptions.notfound;

public class CompraNotFoundException extends RuntimeException {

    public CompraNotFoundException(String message, Throwable problema) {
        super(message, problema);
    }

    public CompraNotFoundException(String message) {
        super(message);
    }
}
