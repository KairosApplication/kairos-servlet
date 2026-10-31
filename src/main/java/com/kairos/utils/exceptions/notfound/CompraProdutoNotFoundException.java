package com.kairos.utils.exceptions.notfound;

public class CompraProdutoNotFoundException extends RuntimeException {

    public CompraProdutoNotFoundException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public CompraProdutoNotFoundException(String mensagem) {
        super(mensagem);
    }
}
