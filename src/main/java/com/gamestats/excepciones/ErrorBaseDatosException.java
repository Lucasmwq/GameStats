package com.gamestats.excepciones;

public class ErrorBaseDatosException extends RuntimeException {
    public ErrorBaseDatosException(String message, Throwable causa) {
        super(message, causa);
    }
}
