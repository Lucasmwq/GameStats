package com.gamestats.api;

import com.gamestats.excepciones.RespuestaApiInvalidaException;
import com.gamestats.excepciones.JuegoNoEncontradoException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RawgApi {
    static String API_KEY = "4c4dcf7d58c74724b270ea6b2b7a26ad";
    static String URLBase = "https://api.rawg.io/api/games";

    public String buscarJuegos(String nombreJuego) throws RespuestaApiInvalidaException, JuegoNoEncontradoException {
        StringBuilder informationString = new StringBuilder();
        try {
            URL url = new URL(URLBase + "?key=" + API_KEY + "&search=" + nombreJuego.replace(" ", "%20"));
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.connect();

            int codigoRespuesta = conexion.getResponseCode();

            if (codigoRespuesta != 200){
                throw new RespuestaApiInvalidaException("Error al conectar con RAWG. Código HTTP: " + codigoRespuesta);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()){
                    informationString.append(sc.nextLine());
                }
                sc.close();
            }
        } catch (RespuestaApiInvalidaException e) {
            throw e;
        } catch (Exception e) {
            throw new RespuestaApiInvalidaException("Ocurrió un error inesperado al realizar la petición: " + e.getMessage());
        }

        String jsonFinal = informationString.toString();

        if (jsonFinal.contains("\"count\": 0") || jsonFinal.contains("\"count\":0")) {
            throw new JuegoNoEncontradoException("No se encontraron resultados para el juego: " + nombreJuego);
        }

        return jsonFinal;
    }
}