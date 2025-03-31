package com.grupo2.diabetter.exception;

public class CommerceException extends RuntimeException {
    public CommerceException() {
        super("Erro inesperado no AppCommerce!");
    }

    public CommerceException(String message) {
        super(message);
    }
}

