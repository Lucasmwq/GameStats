package com.gamestats;

import com.gamestats.api.ParsearJSON;
import com.gamestats.api.RawgApi;
import com.gamestats.basededatos.JuegoBD;
import com.gamestats.modelo.Juego;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        // Crear tabla si no existe
        JuegoBD juegoBD = new JuegoBD();
        juegoBD.crearTabla();

        // Buscar juego
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el juego a buscar: ");
        RawgApi rawgApi = new RawgApi();
        String json = rawgApi.buscarJuegos(sc.nextLine());

        // Parsear JSON
        ParsearJSON parser = new ParsearJSON();
        List<Juego> juegos = parser.parsearJuegos(json);

        // Mostrar resultados
        for (int i = 0; i < juegos.size(); i++) {
            Juego j = juegos.get(i);
            System.out.println("[" + i + "] " + j.getName() + " - " + j.getRating());
        }

        System.out.print("Ingrese el número del juego a guardar: ");
        int eleccion = sc.nextInt();

        Juego juegoElegido = juegos.get(eleccion);
        juegoBD.guardar(juegoElegido);
    }
}