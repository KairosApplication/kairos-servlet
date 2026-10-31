package com.kairos.utils.exceptions.exists;

// Exceção para cpf já existente
public class CpfExistsException extends RuntimeException {

    public CpfExistsException(String mensagem) {
        super(mensagem);
    }

    public CpfExistsException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
