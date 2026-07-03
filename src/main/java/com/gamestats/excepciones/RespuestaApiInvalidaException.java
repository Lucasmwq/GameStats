package com.gamestats.excepciones;

public class RespuestaApiInvalidaException extends Exception{

    public RespuestaApiInvalidaException(String mensaje) {
        super(mensaje);
    }
}
