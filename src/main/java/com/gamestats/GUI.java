package com.gamestats;

import com.gamestats.excepciones.JuegoNoEncontradoException;
import com.gamestats.excepciones.RespuestaApiInvalidaException;
import com.gamestats.excepciones.ErrorBaseDatosException;
import com.gamestats.api.ParsearJSON;
import com.gamestats.api.RawgApi;
import com.gamestats.basededatos.JuegoBD;
import com.gamestats.modelo.EstadoJuego;
import com.gamestats.modelo.Juego;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;

public class GUI extends Application {

    private List<Juego> coleccion;
    private List<Juego> juegos;

    private void recargarColeccion(ListView<String> listaColeccion, List<Juego> coleccion, JuegoBD juegoBD) {
        coleccion = juegoBD.obtenerTodos();
        listaColeccion.getItems().clear();
        coleccion.stream()
                .map(j -> j.getName() + " — Calificación: " + j.getRating() + " — Estado: " + j.getEstado())
                .forEach(listaColeccion.getItems()::add);
    }

    @Override
    public void start(Stage primaryStage) {

        JuegoBD juegoBD = new JuegoBD();
        juegoBD.crearTabla();

        String estiloBotonPrimario = "-fx-font-size: 13px; -fx-padding: 10 30; -fx-cursor: hand; -fx-background-radius: 6;";
        String estiloBotonSecundario = "-fx-font-size: 12px; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 6;";

        //Pantalla de bienvenida
        VBox rootBienvenida = new VBox(30);
        rootBienvenida.setAlignment(Pos.CENTER);
        rootBienvenida.setPadding(new Insets(40));

        Label lblBienvenida = new Label("GameStats");
        lblBienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 36));

        Label lblSubtitulo = new Label("Tu colección de videojuegos personal");
        lblSubtitulo.setFont(new Font("Arial", 14));

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setStyle(estiloBotonPrimario);

        rootBienvenida.getChildren().addAll(lblBienvenida, lblSubtitulo, btnIngresar);
        Scene escenaBienvenida = new Scene(rootBienvenida, 650, 400);

        // Pantalla de menú
        VBox rootMenu = new VBox(15);
        rootMenu.setAlignment(Pos.CENTER);
        rootMenu.setPadding(new Insets(40));

        Label lblSeleccion = new Label("¿Qué quieres hacer?");
        lblSeleccion.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Button btnBuscarJuego = new Button("🔍  Buscar juego");
        btnBuscarJuego.setStyle(estiloBotonPrimario);
        btnBuscarJuego.setMinWidth(200);

        Button btnMiColeccion = new Button("🎮  Mi colección");
        btnMiColeccion.setStyle(estiloBotonPrimario);
        btnMiColeccion.setMinWidth(200);

        rootMenu.getChildren().addAll(lblSeleccion, btnBuscarJuego, btnMiColeccion);
        Scene escenaMenu = new Scene(rootMenu, 650, 400);

        // Pantalla de búsqueda
        VBox rootBusqueda = new VBox(12);
        rootBusqueda.setAlignment(Pos.CENTER);
        rootBusqueda.setPadding(new Insets(30));

        Label lblBuscar = new Label("Buscar videojuego");
        lblBuscar.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        HBox filaBusqueda = new HBox(10);
        filaBusqueda.setAlignment(Pos.CENTER);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Nombre del juego...");
        txtBuscar.setPrefWidth(280);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBotonPrimario);

        filaBusqueda.getChildren().addAll(txtBuscar, btnBuscar);

        ListView<String> listaJuegos = new ListView<>();
        listaJuegos.setPrefWidth(550);
        listaJuegos.setPrefHeight(280);

        HBox filaBotonesGuardar = new HBox(10);
        filaBotonesGuardar.setAlignment(Pos.CENTER);

        Button btnGuardar = new Button("Guardar juego");
        btnGuardar.setStyle(estiloBotonPrimario);

        Button btnVolverBusqueda = new Button("Volver");
        btnVolverBusqueda.setStyle(estiloBotonSecundario);

        filaBotonesGuardar.getChildren().addAll(btnGuardar, btnVolverBusqueda);

        Label lblMensajeBusqueda = new Label();

        rootBusqueda.getChildren().addAll(lblBuscar, filaBusqueda, listaJuegos, filaBotonesGuardar, lblMensajeBusqueda);
        Scene escenaBusqueda = new Scene(rootBusqueda, 650, 520);

        // Pantalla Mi colección
        VBox rootColeccion = new VBox(12);
        rootColeccion.setAlignment(Pos.CENTER);
        rootColeccion.setPadding(new Insets(30));

        Label lblColeccion = new Label("Mi colección");
        lblColeccion.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        ListView<String> listaColeccion = new ListView<>();
        listaColeccion.setPrefWidth(550);
        listaColeccion.setPrefHeight(250);

        HBox filaEstado = new HBox(10);
        filaEstado.setAlignment(Pos.CENTER);

        Label lblEstado = new Label("Estado:");
        String[] opciones = {"Jugando", "Completado", "Pendiente"};
        ChoiceBox<String> tiposDeEstados = new ChoiceBox<>(FXCollections.observableArrayList(opciones));
        tiposDeEstados.setPrefWidth(130);

        Button btnGuardarEstado = new Button("Cambiar estado");
        btnGuardarEstado.setStyle(estiloBotonSecundario);

        filaEstado.getChildren().addAll(lblEstado, tiposDeEstados, btnGuardarEstado);

        HBox filaBotonesColeccion = new HBox(10);
        filaBotonesColeccion.setAlignment(Pos.CENTER);

        Button btnEliminarJuego = new Button("Eliminar juego");
        btnEliminarJuego.setStyle(estiloBotonSecundario);

        Button btnVolverColeccion = new Button("Volver");
        btnVolverColeccion.setStyle(estiloBotonSecundario);

        filaBotonesColeccion.getChildren().addAll(btnEliminarJuego, btnVolverColeccion);

        Label lblMensajeColeccion = new Label();

        rootColeccion.getChildren().addAll(lblColeccion, listaColeccion, filaEstado, filaBotonesColeccion, lblMensajeColeccion);
        Scene escenaColeccion = new Scene(rootColeccion, 650, 520);

        // Lógica de búsqueda
        RawgApi rawgApi = new RawgApi();
        ParsearJSON parser = new ParsearJSON();

        btnBuscar.setOnAction(e -> {
            String nombre = txtBuscar.getText().trim();
            if (!nombre.isEmpty()) {
                try {
                    String json = rawgApi.buscarJuegos(nombre);
                    juegos = parser.parsearJuegos(json);
                    listaJuegos.getItems().clear();
                    juegos.stream()
                            .map(j -> j.getName() + " — Calificación: " + j.getRating())
                            .forEach(listaJuegos.getItems()::add);
                    lblMensajeBusqueda.setText("");

                } catch (JuegoNoEncontradoException ex) {
                    listaJuegos.getItems().clear();
                    lblMensajeBusqueda.setText(ex.getMessage());
                } catch (RespuestaApiInvalidaException ex) {
                    lblMensajeBusqueda.setText("Error de conexión con la API de juegos.");
                    ex.printStackTrace();
                } catch (Exception ex) {
                    lblMensajeBusqueda.setText("Ocurrió un error inesperado.");
                    ex.printStackTrace();
                }
            }
        });

        // Lógica de guardado
        btnGuardar.setOnAction(e -> {
            int indice = listaJuegos.getSelectionModel().getSelectedIndex();
            if (indice >= 0 && juegos != null) {
                Juego elegido = juegos.get(indice);
                try {
                    juegoBD.guardar(elegido, EstadoJuego.PENDIENTE);
                    lblMensajeBusqueda.setText("Guardado: " + elegido.getName());
                } catch (ErrorBaseDatosException ex) {
                    lblMensajeBusqueda.setText("Error al acceder a la base de datos.");
                    ex.printStackTrace();
                }
            } else {
                lblMensajeBusqueda.setText("Selecciona un juego primero.");
            }
        });

        // Lógica de colección
        btnMiColeccion.setOnAction(e -> {
            coleccion = juegoBD.obtenerTodos();
            if (coleccion.isEmpty()) {
                lblMensajeColeccion.setText("No tienes juegos guardados aún");
            } else {
                recargarColeccion(listaColeccion, coleccion, juegoBD);
            }
            lblMensajeColeccion.setText("");
            primaryStage.setScene(escenaColeccion);
        });

        btnEliminarJuego.setOnAction(e -> {
            int indice = listaColeccion.getSelectionModel().getSelectedIndex();
            if (indice >= 0) {
                Juego juegoEliminar = coleccion.get(indice);
                juegoBD.eliminarJuego(juegoEliminar.getId());
                recargarColeccion(listaColeccion, coleccion, juegoBD);
                lblMensajeColeccion.setText("Juego eliminado correctamente");
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        btnGuardarEstado.setOnAction(event -> {
            int indice = listaColeccion.getSelectionModel().getSelectedIndex();
            if (indice >= 0) {
                Juego juegoACambiarEstado = coleccion.get(indice);
                EstadoJuego estado = EstadoJuego.valueOf(tiposDeEstados.getValue().toUpperCase());
                juegoBD.actualizarEstadoJuego(estado, juegoACambiarEstado.getId());
                recargarColeccion(listaColeccion, coleccion, juegoBD);
                lblMensajeColeccion.setText("Estado cambiado correctamente");
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        // Navegación
        btnIngresar.setOnAction(e -> primaryStage.setScene(escenaMenu));
        btnBuscarJuego.setOnAction(e -> {
            listaJuegos.getItems().clear();
            txtBuscar.clear();
            lblMensajeBusqueda.setText("");
            primaryStage.setScene(escenaBusqueda);
        });
        btnVolverBusqueda.setOnAction(e -> primaryStage.setScene(escenaMenu));
        btnVolverColeccion.setOnAction(e -> primaryStage.setScene(escenaMenu));

        // Config ventana STAGE
        primaryStage.setTitle("GameStats");
        primaryStage.setScene(escenaBienvenida);
        primaryStage.show();
    }
}