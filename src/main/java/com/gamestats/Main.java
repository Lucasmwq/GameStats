package com.gamestats;

import com.gamestats.api.RawgApi;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RawgApi rawgApi = new RawgApi();

        System.out.println("Ingrese el juego a buscar");
        System.out.println(rawgApi.buscarJuegos(sc.nextLine()));
    }
}
