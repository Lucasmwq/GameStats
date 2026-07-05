package com.gamestats.modelo;

public interface Progresable {
    void sumarTiempoJugado(int minutos);
    double getTiempoJugadoDecimal();
    String obtenerResumenProgreso();
}