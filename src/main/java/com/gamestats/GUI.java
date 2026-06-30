package com.gamestats;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        //Pantalla de bienvenida

        VBox rootBienvenida = new VBox(20);
        rootBienvenida.setAlignment(Pos.CENTER);

        Label lblBienvenida = new Label ("Bienvenido/a a GameStats!");
        lblBienvenida.setFont(new Font("Arial", 24));

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-cursor: hand;");

        rootBienvenida.getChildren().addAll(lblBienvenida, btnIngresar);
        Scene escenaBienvenida = new Scene(rootBienvenida, 600, 400);

        //Pantalla de ingreso

        VBox rootMenu = new VBox(20);
        rootMenu.setAlignment(Pos.CENTER);

        Label lblSeleccion = new Label ("Seleccione una opción");
        lblSeleccion.setFont(new Font("Arial", 20));

        Button btnHoras = new Button("Horas jugadas");
        btnIngresar.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand;");

        Button btnCalificaciones = new Button("Calificaciones");
        btnIngresar.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand;");

        rootMenu.getChildren().addAll(lblSeleccion, btnHoras,btnCalificaciones);
        Scene escenaMenu = new Scene(rootMenu, 600, 400);

        btnIngresar.setOnAction(e-> {
            primaryStage.setScene(escenaMenu);
        });

        btnHoras.setOnAction(e-> {
            System.out.println("Aquí se integrará la vista de horas jugadas");
        });

        btnCalificaciones.setOnAction(e-> {
            System.out.println("Aquí se integrará la busqueda de RAWG API.");
        });

        //Config de la ventana STAGE

        primaryStage.setTitle("GameStats");
        primaryStage.setScene(escenaBienvenida);
        primaryStage.show();
    }

}
