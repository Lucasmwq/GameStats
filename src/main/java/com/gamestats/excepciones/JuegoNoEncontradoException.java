package com.gamestats.excepciones;

public class JuegoNoEncontradoException extends RuntimeException {
    public JuegoNoEncontradoException(String message) {
        super(message);
    }
}
