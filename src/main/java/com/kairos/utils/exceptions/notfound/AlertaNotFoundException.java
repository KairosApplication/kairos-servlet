package com.kairos.utils.exceptions.notfound;

public class AlertaNotFoundException extends RuntimeException {

    public AlertaNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public AlertaNotFoundException(String mensagem) {
        super(mensagem);
    }
}
