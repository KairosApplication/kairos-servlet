package com.kairos.utils.exceptions.quantity;

public class ReplenishmentQuantityException extends RuntimeException {

    public ReplenishmentQuantityException(String message, Throwable problema) {
        super(message, problema);
    }

    public ReplenishmentQuantityException(String message) {
        super(message);
    }
}
