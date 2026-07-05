package com.gamestats.modelo;

public abstract class JuegoBase {
    protected int id;
    protected String name;
    protected String background_image;
    protected double rating;

    public JuegoBase() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBackground_image() { return background_image; }
    public void setBackground_image(String background_image) { this.background_image = background_image; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    // Método abstracto: obliga a las clases hijas a definir cómo mostrar su info base
    public abstract String obtenerInformacionBasica();
}