package com.kairos.utils.exceptions.quantity;

public class PurchaseQuantityException extends RuntimeException {

    public PurchaseQuantityException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public PurchaseQuantityException(String mensagem) {
        super(mensagem);
    }
}
