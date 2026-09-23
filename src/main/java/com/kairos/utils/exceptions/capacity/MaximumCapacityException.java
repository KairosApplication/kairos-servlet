package com.kairos.utils.exceptions.capacity;

public class MaximumCapacityException extends RuntimeException {

    public MaximumCapacityException(String mensagem) {
        super(mensagem);
    }

    public MaximumCapacityException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
