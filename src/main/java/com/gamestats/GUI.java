package com.gamestats;

import com.gamestats.api.ParsearJSON;
import com.gamestats.api.RawgApi;
import com.gamestats.basededatos.JuegoBD;
import com.gamestats.modelo.Juego;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;

public class GUI extends Application {

    private List<Juego> coleccion;

    @Override
    public void start(Stage primaryStage) {

        JuegoBD juegoBD = new JuegoBD();
        juegoBD.crearTabla();

        // ── Pantalla de bienvenida ──
        VBox rootBienvenida = new VBox(20);
        rootBienvenida.setAlignment(Pos.CENTER);

        Label lblBienvenida = new Label ("Bienvenido/a a GameStats!");
        lblBienvenida.setFont(new Font("Arial", 24));

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-cursor: hand;");

        rootBienvenida.getChildren().addAll(lblBienvenida, btnIngresar);
        Scene escenaBienvenida = new Scene(rootBienvenida, 600, 400);

        // Pantalla de menú
        VBox rootMenu = new VBox(20);
        rootMenu.setAlignment(Pos.CENTER);

        Label lblSeleccion = new Label ("Seleccione una opción");
        lblSeleccion.setFont(new Font("Arial", 20));

        Button btnBuscarJuego = new Button("Buscar juego");
        btnBuscarJuego.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand;");

        Button btnMiColeccion = new Button("Mi colección");
        btnMiColeccion.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand;");

        rootMenu.getChildren().addAll(lblSeleccion, btnBuscarJuego, btnMiColeccion);
        Scene escenaMenu = new Scene(rootMenu, 600, 400);

        // ── Pantalla de búsqueda ──
        VBox rootBusqueda = new VBox(15);
        rootBusqueda.setAlignment(Pos.CENTER);

        Label lblBuscar = new Label("Buscar juego");
        lblBuscar.setFont(new Font("Arial", 20));

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Ingrese el nombre del juego...");
        txtBuscar.setMaxWidth(300);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-font-size: 14px; -fx-padding: 8 20; -fx-cursor: hand;");

        ListView<String> listaJuegos = new ListView<>();
        listaJuegos.setMaxWidth(400);
        listaJuegos.setMaxHeight(200);

        Button btnGuardar = new Button("Guardar juego seleccionado");
        btnGuardar.setStyle("-fx-font-size: 14px; -fx-padding: 8 20; -fx-cursor: hand;");

        Button btnVolverBusqueda = new Button("Volver");
        btnVolverBusqueda.setStyle("-fx-font-size: 12px; -fx-padding: 6 16; -fx-cursor: hand;");

        Label lblMensajeBusqueda = new Label();

        rootBusqueda.getChildren().addAll(lblBuscar, txtBuscar, btnBuscar, listaJuegos, btnGuardar, btnVolverBusqueda, lblMensajeBusqueda);
        Scene escenaBusqueda = new Scene(rootBusqueda, 600, 500);

        // ── Pantalla Mi colección ──
        VBox rootColeccion = new VBox(15);
        rootColeccion.setAlignment(Pos.CENTER);

        Label lblColeccion = new Label("Mi colección");
        lblColeccion.setFont(new Font("Arial", 20));

        ListView<String> listaColeccion = new ListView<>();
        listaColeccion.setMaxWidth(400);
        listaColeccion.setMaxHeight(250);

        Button btnVolverColeccion = new Button("Volver");
        btnVolverColeccion.setStyle("-fx-font-size: 12px; -fx-padding: 6 16; -fx-cursor: hand;");

        Button btnEliminarJuego = new Button("Eliminar Juego");
        btnEliminarJuego.setStyle("-fx-font-size: 14px; -fx-padding: 8 20; -fx-cursor: hand;");

        Label lblMensajeColeccion = new Label();

        rootColeccion.getChildren().addAll(lblColeccion, listaColeccion, btnEliminarJuego, btnVolverColeccion, lblMensajeColeccion);
        Scene escenaColeccion = new Scene(rootColeccion, 600, 500);




        // ── Lógica de búsqueda ──
        RawgApi rawgApi = new RawgApi();
        ParsearJSON parser = new ParsearJSON();
        final List<Juego>[] juegos = new List[]{null};

        btnBuscar.setOnAction(e -> {
            String nombre = txtBuscar.getText().trim();
            if (!nombre.isEmpty()) {
                try {
                    String json = rawgApi.buscarJuegos(nombre);
                    juegos[0] = parser.parsearJuegos(json);
                    listaJuegos.getItems().clear();
                    for (Juego j : juegos[0]) {
                        listaJuegos.getItems().add(j.getName() + " - " + j.getRating());
                    }
                    lblMensajeBusqueda.setText("");
                } catch (Exception ex) {
                    lblMensajeBusqueda.setText("Error al buscar juegos");
                    ex.printStackTrace();
                }
            }
        });

        btnGuardar.setOnAction(e -> {
            int indice = listaJuegos.getSelectionModel().getSelectedIndex();
            if (indice >= 0 && juegos[0] != null) {
                Juego elegido = juegos[0].get(indice);
                juegoBD.guardar(elegido);
                lblMensajeBusqueda.setText("Guardado: " + elegido.getName());
            } else {
                lblMensajeBusqueda.setText("Selecciona un juego primero");
            }
        });

        // ── Lógica de colección ──

        btnMiColeccion.setOnAction(e -> {
            listaColeccion.getItems().clear();
            coleccion = juegoBD.obtenerTodos();
            if (coleccion.isEmpty()) {
                lblMensajeColeccion.setText("No tienes juegos guardados aún");
            } else {
                for (Juego j : coleccion) {
                    listaColeccion.getItems().add(j.getName() + " - " + j.getRating());
                }
                lblMensajeColeccion.setText("");
            }
            primaryStage.setScene(escenaColeccion);
        });

        // Logica de eliminar los juegos

        btnEliminarJuego.setOnAction(e -> {
            int indice = listaColeccion.getSelectionModel().getSelectedIndex();
            if (indice >= 0) {
                Juego juegoEliminar = coleccion.get(indice);
                juegoBD.eliminarJuego(juegoEliminar.getId());
                listaColeccion.getItems().remove(indice);
                lblMensajeColeccion.setText("Juego eliminado correctamente");
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        // ── Navegación ──
        btnIngresar.setOnAction(e -> primaryStage.setScene(escenaMenu));
        btnBuscarJuego.setOnAction(e -> primaryStage.setScene(escenaBusqueda));
        btnVolverBusqueda.setOnAction(e -> primaryStage.setScene(escenaMenu));
        btnVolverColeccion.setOnAction(e -> primaryStage.setScene(escenaMenu));

        // ── Config ventana ──
        primaryStage.setTitle("GameStats");
        primaryStage.setScene(escenaBienvenida);
        primaryStage.show();
    }
}