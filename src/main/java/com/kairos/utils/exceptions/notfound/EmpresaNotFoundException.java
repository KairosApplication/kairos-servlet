package com.kairos.utils.exceptions.notfound;

public class EmpresaNotFoundException extends RuntimeException {

    public EmpresaNotFoundException(String mensagem) {
        super(mensagem);
    }

    public EmpresaNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
