package com.kairos.utils.exceptions;

public class StockQuantityException extends RuntimeException {

    public StockQuantityException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public StockQuantityException(String mensagem) {
        super(mensagem);
    }
}
