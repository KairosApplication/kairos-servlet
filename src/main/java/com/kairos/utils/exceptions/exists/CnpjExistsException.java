package com.kairos.utils.exceptions.exists;

public class CnpjExistsException extends RuntimeException {

    public CnpjExistsException(String mensagem) {
        super(mensagem);
    }

    public CnpjExistsException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
