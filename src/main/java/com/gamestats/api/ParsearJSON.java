package com.gamestats.api;

import com.gamestats.modelo.Juego;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class ParsearJSON {

    public List<Juego> parsearJuegos(String json) {
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(json, JsonObject.class);
        JsonArray resultados = objeto.getAsJsonArray("results");

        List<Juego> juegos = new ArrayList<>();
        for (int i = 0; i < resultados.size(); i++) {
            Juego juego = gson.fromJson(resultados.get(i), Juego.class);
            juegos.add(juego);
        }
        return juegos;
    }
}