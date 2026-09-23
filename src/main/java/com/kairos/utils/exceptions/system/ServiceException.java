package com.kairos.utils.exceptions.system;

// Utilizado para exceções no Service
public class ServiceException extends RuntimeException {

    public ServiceException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public ServiceException(String mensagem) {
        super(mensagem);
    }
}
