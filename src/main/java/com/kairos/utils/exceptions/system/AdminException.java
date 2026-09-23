package com.kairos.utils.exceptions.system;

public class AdminException extends RuntimeException {

    public AdminException(String mensagem) {
        super(mensagem);
    }

    public AdminException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
