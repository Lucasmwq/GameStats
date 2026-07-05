package com.gamestats.modelo;

public class Juego extends ElementoCatalogo implements Gestionable {

    private boolean favorito;
    private int tiempoJugadoMinutos;
    private String name;
    private String background_image;
    private double rating;
    private EstadoJuego estado;

    public Juego(int id, String nombre, String name) {
        super(id, nombre);
        this.name = name;
    }

    public Juego() {
        super(0,"");
    }

    @Override
    public void guardar() {
        System.out.println("Guardando: " + name);
    }

    @Override
    public void eliminar() {
        System.out.println("Eliminando: " + name);
    }

    @Override
    public String obtenerResumen() {
        return name + " - Calificación: " + rating + " - Estado: " + estado;
    }

    public EstadoJuego getEstado() {
        return estado;
    }
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBackground_image() {
        return background_image;
    }
    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    //Agregado para implementar en la interfaz la parte de tiempo jugado (INTERFAZ V1.3)
    public int getTiempoJugadoMinutos() { return tiempoJugadoMinutos;}
    public void setTiempoJugadoMinutos(int tiempoJugadoMinutos) {this.tiempoJugadoMinutos= tiempoJugadoMinutos;}
    public double getTiempoJugadoDecimal() { return this.tiempoJugadoMinutos/60.0;}

    //Agregado para implementar en la interfaz filtrado por juegos favoritos (INTERFAZ V1.4)
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
}