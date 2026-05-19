package com.gamestats.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RawgApi {
    static String API_KEY = "4c4dcf7d58c74724b270ea6b2b7a26ad";
    static String URLBase = "https://api.rawg.io/api/games";

    Scanner sc = new Scanner(System.in);


    public StringBuilder buscarJuegos(String nombreJuego){
        // Solicitar peticion
        StringBuilder informationString = new StringBuilder();
        try {
            URL url = new URL(URLBase + "?key=" + API_KEY + "&search=" + nombreJuego);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.connect();

            // Peticion correcta?
            int codigoRespuesta = conexion.getResponseCode();
            System.out.println(codigoRespuesta);
            if (codigoRespuesta!= 200){
                System.out.println("Ocurrio un error " + codigoRespuesta);
            } else {
                // Scanner que lea los datos
                Scanner sc = new Scanner(url.openStream());

                while (sc.hasNext()){
                    informationString.append(sc.nextLine());
                }

                sc.close();
                // Mostrar la info por consola
                return informationString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return informationString;
    }
}