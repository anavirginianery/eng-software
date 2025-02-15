package com.grupo2.diabetter.exception;


public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super("Password Inválido:");  //adicionar a lógica de construção de um password válido
    }
}