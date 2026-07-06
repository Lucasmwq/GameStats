package com.gamestats.api;

import com.gamestats.excepciones.RespuestaApiInvalidaException;
import com.gamestats.modelo.Juego;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParsearJSON {

    public List<Juego> parsearJuegos(String json) throws RespuestaApiInvalidaException {
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(json, JsonObject.class);
        JsonArray resultados = objeto.getAsJsonArray("results");

        if (resultados == null) {
            throw new RespuestaApiInvalidaException("Estructura JSON inválida o clave ausente.");
        }

        return IntStream.range(0, resultados.size())
                .mapToObj(i -> gson.fromJson(resultados.get(i), Juego.class))
                .collect(Collectors.toList());
    }
}