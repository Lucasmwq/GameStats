package com.gamestats.modelo;

public abstract class ElementoCatalogo implements Gestionable {
    protected int id;
    protected String nombre;

    public ElementoCatalogo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    public abstract String obtenerResumen();
}
