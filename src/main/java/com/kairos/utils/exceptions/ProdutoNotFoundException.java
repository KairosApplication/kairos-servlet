package com.kairos.utils.exceptions;

public class ProdutoNotFoundException extends RuntimeException {

    public ProdutoNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public ProdutoNotFoundException(String mensagem) {
        super(mensagem);
    }
}
