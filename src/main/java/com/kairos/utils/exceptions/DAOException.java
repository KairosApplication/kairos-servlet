package com.kairos.utils.exceptions;

// Utilizado para exceções que ocorrem no DAO
public class DAOException extends RuntimeException {

    public DAOException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }

    public DAOException(String mensagem) {
        super(mensagem);
    }
}
