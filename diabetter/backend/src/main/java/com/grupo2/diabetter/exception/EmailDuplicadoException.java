package com.grupo2.diabetter.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String mensagem) {
        super(mensagem);
    }
}

