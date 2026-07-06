package com.gamestats.modelo;

public class Juego extends JuegoBase implements Progresable {

    private boolean favorito;
    private int tiempoJugadoMinutos;
    private EstadoJuego estado;

    public Juego(int id, String nombre, String imagen, double calificacion) {
        super();
        setId(id);
        setName(nombre);
        setImage(imagen);
        setRating(calificacion);
        this.estado = EstadoJuego.PENDIENTE; // Por defecto
    }

    public Juego() {

    }

    private void setImage(String imagen) {
    }

    // Implementación de la Clase Abstracta
    @Override
    public String obtenerInformacionBasica() {
        return getName() + " - Calificación RAWG: " + getRating();
    }

    // Implementación de la Interfaz Progresable
    @Override
    public void sumarTiempoJugado(int minutos) {
        this.tiempoJugadoMinutos += minutos;
    }

    @Override
    public double getTiempoJugadoDecimal() {
        return this.tiempoJugadoMinutos / 60.0;
    }

    @Override
    public String obtenerResumenProgreso() {
        return "Estado: " + estado + " | Tiempo: " + String.format(java.util.Locale.US, "%.1f", getTiempoJugadoDecimal()) + "h";
    }

    // Getters y Setters
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }

    public int getTiempoJugadoMinutos() { return tiempoJugadoMinutos; }
    public void setTiempoJugadoMinutos(int tiempoJugadoMinutos) { this.tiempoJugadoMinutos = tiempoJugadoMinutos; }

    public EstadoJuego getEstado() { return estado; }
    public void setEstado(EstadoJuego estado) { this.estado = estado; }

    public void setTiempoJugado(int minutos) {
        this.tiempoJugadoMinutos = minutos;
    }
}