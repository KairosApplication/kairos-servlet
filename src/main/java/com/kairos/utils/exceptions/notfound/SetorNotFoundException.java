package com.kairos.utils.exceptions.notfound;

public class SetorNotFoundException extends RuntimeException {

    public SetorNotFoundException(String mensagem) {
        super(mensagem);
    }

    public SetorNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
