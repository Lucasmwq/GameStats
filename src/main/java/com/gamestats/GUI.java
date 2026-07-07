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
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;

public class GUI extends Application {

    private List<Juego> coleccion;
    private List<Juego> juegos;

    private void recargarColeccion(ListView<Juego> listaColeccion, JuegoBD juegoBD, boolean soloFavoritos) {
        this.coleccion = juegoBD.obtenerTodos();
        listaColeccion.getItems().clear();
        this.coleccion.stream()
                .filter(j -> !soloFavoritos || j.isFavorito())
                .forEach(listaColeccion.getItems()::add);
    }

    @Override
    public void start(Stage primaryStage) {

        JuegoBD juegoBD = new JuegoBD();
        juegoBD.crearTabla();

        //Pantalla de bienvenida
        VBox rootBienvenida = new VBox(40);
        rootBienvenida.setAlignment(Pos.CENTER);
        rootBienvenida.setPadding(new Insets(40));

        // GAMESTATS COMO LOGO EN EL INICIO
        ImageView vistaLogo = new ImageView();
        try {
            Image imagenLogo = new Image(getClass().getResourceAsStream("/logo.png"));
            vistaLogo.setImage(imagenLogo);
            vistaLogo.setFitWidth(700);
            vistaLogo.setPreserveRatio(true);
        } catch (NullPointerException e) {
            System.err.println("Advertencia: No se encontró el archivo logo.png en resources.");
        }

        Label lblSubtitulo = new Label("Tu colección de videojuegos personal");
        // aumentamos el tamaño de la fuente ya que se veia demasiado pequeña
        lblSubtitulo.setStyle("-fx-font-size: 24px; -fx-text-fill: lightgray;");

        Button btnIngresar = new Button("Ingresar");
        // tambien aumentamos el tamaño del boton
        btnIngresar.setPrefWidth(350);
        btnIngresar.setStyle("-fx-padding: 15; -fx-font-size: 22px; -fx-cursor: hand; -fx-font-weight: bold;");

        rootBienvenida.getChildren().addAll(vistaLogo, lblSubtitulo, btnIngresar);
        Scene escenaBienvenida = new Scene(rootBienvenida);
        escenaBienvenida.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Pantalla de menú
        VBox rootMenu = new VBox(30);
        rootMenu.setAlignment(Pos.CENTER);
        rootMenu.setPadding(new Insets(40));

        Label lblSeleccion = new Label("¿Qué quieres hacer?");
        lblSeleccion.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button btnBuscarJuego = new Button("🔍  Buscar juego");
        btnBuscarJuego.setMinWidth(450);
        btnBuscarJuego.setStyle("-fx-padding: 20; -fx-font-size: 22px; -fx-cursor: hand;");

        Button btnMiColeccion = new Button("🎮  Mi colección");
        btnMiColeccion.setMinWidth(450);
        btnMiColeccion.setStyle("-fx-padding: 20; -fx-font-size: 22px; -fx-cursor: hand;");

        rootMenu.getChildren().addAll(lblSeleccion, btnBuscarJuego, btnMiColeccion);

        // Pantalla de búsqueda
        VBox rootBusqueda = new VBox(12);
        rootBusqueda.setAlignment(Pos.CENTER);
        rootBusqueda.setPadding(new Insets(30));

        Label lblBuscar = new Label("Buscar videojuego");

        HBox filaBusqueda = new HBox(10);
        filaBusqueda.setAlignment(Pos.CENTER);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Nombre del juego...");
        txtBuscar.setPrefWidth(280);

        Button btnBuscar = new Button("Buscar");

        filaBusqueda.getChildren().addAll(txtBuscar, btnBuscar);

        ListView<Juego> listaJuegos = new ListView<>();
        listaJuegos.setMaxWidth(900);
        VBox.setVgrow(listaJuegos, Priority.ALWAYS);
        // cell factory para renderizar las portadas en la pantalla de busqueda
        listaJuegos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Juego j, boolean empty) {
                super.updateItem(j, empty);
                if (empty || j == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView vistaPortada = new ImageView();
                    vistaPortada.setFitWidth(160); // Tamaño un poco más compacto que la colección
                    vistaPortada.setFitHeight(90);
                    vistaPortada.setPreserveRatio(true);

                    if (j.getBackground_image() != null && !j.getBackground_image().isEmpty()) {
                        Image imagen = new Image(j.getBackground_image(), true);
                        vistaPortada.setImage(imagen);
                    }

                    // Solo mostramos la información básica (sin estado ni horas, porque aún no se guarda)
                    Label lblTexto = new Label(j.obtenerInformacionBasica());
                    lblTexto.setPadding(new Insets(0, 0, 0, 20));

                    HBox fila = new HBox(vistaPortada, lblTexto);
                    fila.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(fila);
                }
            }
        });

        HBox filaBotonesGuardar = new HBox(10);
        filaBotonesGuardar.setAlignment(Pos.CENTER);

        Button btnGuardar = new Button("Guardar juego");

        Button btnVolverBusqueda = new Button("Volver");

        filaBotonesGuardar.getChildren().addAll(btnGuardar, btnVolverBusqueda);

        Label lblMensajeBusqueda = new Label();

        rootBusqueda.getChildren().addAll(lblBuscar, filaBusqueda, listaJuegos, filaBotonesGuardar, lblMensajeBusqueda);

        // Pantalla Mi colección
        VBox rootColeccion = new VBox(12);
        rootColeccion.setAlignment(Pos.CENTER);
        rootColeccion.setPadding(new Insets(30));

        Label lblColeccion = new Label("Mi colección");

        // [INTERFAZ V1_4] - CELL FACTORY PARA RENDERIZAR LA ESTRELLA Y TEXTO

        ToggleButton btnFiltroFavoritos = new ToggleButton("☆");
        btnFiltroFavoritos.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");

        ListView<Juego> listaColeccion = new ListView<>();
        listaColeccion.setMaxWidth(900);
        VBox.setVgrow(listaColeccion, Priority.ALWAYS);

        listaColeccion.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Juego j, boolean empty) {
                super.updateItem(j, empty);
                if (empty || j == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label lblEstrella = new Label(j.isFavorito() ? "★ " : "☆ ");
                    lblEstrella.setStyle(j.isFavorito() ? "-fx-text-fill: gold; -fx-font-size: 18px; -fx-cursor: hand;" : "-fx-text-fill: gray; -fx-font-size: 18; -fx-cursor: hand;");

                    // Evento interactivo sobre la estrella individual
                    lblEstrella.setOnMouseClicked(event -> {
                        juegoBD.actualizarFavorito(j.getId(), !j.isFavorito());

                        // Busca si el boón de filtro está activo leyendo la estructura
                        recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
                    });

                    // Logica para mostrar la imagen del juego
                    ImageView vistaPortada = new ImageView();
                    vistaPortada.setFitWidth(240);
                    vistaPortada.setFitHeight(135);
                    vistaPortada.setPreserveRatio(true);

                    if (j.getBackground_image() != null && !j.getBackground_image().isEmpty()) {
                        Image imagen = new Image(j.getBackground_image(), true);
                        vistaPortada.setImage(imagen);
                    }

                    Label lblTexto = new Label(j.obtenerInformacionBasica() + "\n" + j.obtenerResumenProgreso());
                    lblTexto.setPadding(new Insets(0, 0, 0, 20));

                    HBox fila = new HBox(lblEstrella, vistaPortada,lblTexto);
                    fila.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(fila);
                }
            }
        });

        HBox filaEstado = new HBox(10);
        filaEstado.setAlignment(Pos.CENTER);

        Label lblEstado = new Label("Estado:");
        String[] opciones = {"Jugando", "Completado", "Pendiente", "Continuo"};
        ChoiceBox<String> tiposDeEstados = new ChoiceBox<>(FXCollections.observableArrayList(opciones));
        tiposDeEstados.setPrefWidth(130);

        Button btnGuardarEstado = new Button("Cambiar estado");
        Button btnAgregarTiempo = new Button("Agregar tiempo jugado");
        Button btnCambiarPortada = new Button("Cambiar portada");

        filaEstado.getChildren().addAll(lblEstado, tiposDeEstados, btnGuardarEstado, btnAgregarTiempo, btnCambiarPortada, btnFiltroFavoritos);

        HBox filaBotonesColeccion = new HBox(10);
        filaBotonesColeccion.setAlignment(Pos.CENTER);

        Button btnEliminarJuego = new Button("Eliminar juego");

        Button btnVolverColeccion = new Button("Volver");

        filaBotonesColeccion.getChildren().addAll(btnEliminarJuego, btnVolverColeccion);

        Label lblMensajeColeccion = new Label();

        rootColeccion.getChildren().addAll(lblColeccion, listaColeccion, filaEstado, filaBotonesColeccion, lblMensajeColeccion);

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
                            .filter(j -> j.getRating() > 0.0)
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

        // [INTERFAZ V1_4] BOTON DE FILTRO GLOBAL UTILIZANDO TOGGLE BUTTON
        btnFiltroFavoritos.setOnAction(e -> {

            if (btnFiltroFavoritos.isSelected()) {
                btnFiltroFavoritos.setText("★");
                btnFiltroFavoritos.setStyle("-fx-font-size: 16px; -fx-cursor: hand; -fx-text-fill: gold");
                recargarColeccion(listaColeccion, juegoBD, true);
            } else {

                btnFiltroFavoritos.setText("☆");
                btnFiltroFavoritos.setStyle("-fx-font-size: 16px; -fx-cursor: hand; -fx-text-fill: black");
                recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
            }
        });

        // Lógica de guardado
        btnGuardar.setOnAction(e -> {
            Juego elegido = listaJuegos.getSelectionModel().getSelectedItem();
            if (elegido != null) {
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
                recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
            }
            lblMensajeColeccion.setText("");
            escenaBienvenida.setRoot(rootColeccion);
        });

        btnEliminarJuego.setOnAction(e -> {
            Juego juegoEliminar = listaColeccion.getSelectionModel().getSelectedItem();
            if (juegoEliminar != null) {
                juegoBD.eliminarJuego(juegoEliminar.getId());
                recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
                lblMensajeColeccion.setText("Juego eliminado correctamente");
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        btnGuardarEstado.setOnAction(event -> {
            Juego juegoACambiarEstado = listaColeccion.getSelectionModel().getSelectedItem();
            if (juegoACambiarEstado!= null && tiposDeEstados.getValue() != null) {
                EstadoJuego estado = EstadoJuego.valueOf(tiposDeEstados.getValue().toUpperCase());
                juegoBD.actualizarEstadoJuego(estado, juegoACambiarEstado.getId());
                recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
                lblMensajeColeccion.setText("Estado cambiado correctamente");
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        // [INTERFAZ V1.4] (AGREGAR BOTÓN PARA SUMAR HORAS JUGADAS Y FAVORITOS)
        btnAgregarTiempo.setOnAction(event -> {
            Juego juegoSeleccionado = listaColeccion.getSelectionModel().getSelectedItem();
            //OBTIENE EL ÍNDICE NÚMERICO SELEECIONADO EN LA LISTA
            if (juegoSeleccionado!=null) {

                Stage ventanaTiempo = new Stage();
                ventanaTiempo.setTitle("Tiempo: " + juegoSeleccionado.getName());

                VBox layoutTiempo = new VBox(15);
                layoutTiempo.setAlignment(Pos.CENTER);
                layoutTiempo.setPadding(new Insets(20));

                HBox filaInputs = new HBox(10);
                filaInputs.setAlignment(Pos.CENTER);

                TextField txtHoras = new TextField("0");
                txtHoras.setPrefWidth(50);
                Label lblHoras = new Label("Horas");

                TextField txtMin = new TextField("0");
                txtMin.setPrefWidth(50);
                Label lblMin = new Label("Minutos");

                filaInputs.getChildren().addAll(txtHoras, lblHoras, txtMin, lblMin);

                Button btnConfirmarTiempo = new Button("Sumar Tiempo");

                Label lblErrorTiempo = new Label();
                lblErrorTiempo.setStyle("-fx-text-fill: red;");

                //EVENTO INTERNO DEL BOTÓN DE CONFIRMACIÓN DE LA VENTANA MODAL
                btnConfirmarTiempo.setOnAction(e -> {
                    try {
                        int horas = Integer.parseInt(txtHoras.getText());
                        int minutos = Integer.parseInt(txtMin.getText());
                        int minutosTotales = (horas * 60) + minutos;

                        juegoBD.sumarTiempoJugado(juegoSeleccionado.getId(), minutosTotales);
                        recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
                        lblMensajeColeccion.setText("Tiempo guardado en "+ juegoSeleccionado.getName());
                        ventanaTiempo.close();
                    } catch (NumberFormatException ex) {
                        lblErrorTiempo.setText("Formato numérico inválido");
                    }
                });

                layoutTiempo.getChildren().addAll(new Label("Registrar tiempo adicional:"), filaInputs, btnConfirmarTiempo, lblErrorTiempo);

                Scene escenaTiempo = new Scene(layoutTiempo, 350, 200);
                escenaTiempo.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                ventanaTiempo.setScene(escenaTiempo);
                ventanaTiempo.show();
            } else {
                lblMensajeColeccion.setText("Selección requerida.");
            }
        });

        // Lógica del botón para cambiar la portada
        btnCambiarPortada.setOnAction(event -> {
            Juego juegoSeleccionado = listaColeccion.getSelectionModel().getSelectedItem();

            if (juegoSeleccionado != null) {
                TextInputDialog dialogo = new TextInputDialog(juegoSeleccionado.getBackground_image());
                dialogo.setTitle("Cambiar Portada");
                dialogo.setHeaderText("Modificar imagen de: " + juegoSeleccionado.getName());
                dialogo.setContentText("Pega el link directo de la nueva imagen:");

                // Si el usuario presiona "OK", se actualiza la BD y la lista
                dialogo.showAndWait().ifPresent(nuevaUrl -> {
                    juegoBD.actualizarPortada(juegoSeleccionado.getId(), nuevaUrl);
                    recargarColeccion(listaColeccion, juegoBD, btnFiltroFavoritos.isSelected());
                    lblMensajeColeccion.setText("Portada actualizada");
                });
            } else {
                lblMensajeColeccion.setText("Selecciona un juego primero");
            }
        });

        // Navegación
        btnIngresar.setOnAction(e -> escenaBienvenida.setRoot(rootMenu));
        btnBuscarJuego.setOnAction(e -> {
            listaJuegos.getItems().clear();
            txtBuscar.clear();
            lblMensajeBusqueda.setText("");
            escenaBienvenida.setRoot(rootBusqueda);
        });
        btnVolverBusqueda.setOnAction(e -> escenaBienvenida.setRoot(rootMenu));
        btnVolverColeccion.setOnAction(e -> escenaBienvenida.setRoot(rootMenu));

        // Config ventana STAGE
        primaryStage.setTitle("GameStats");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/icono.png")));
        primaryStage.setMaximized(true);
        primaryStage.setScene(escenaBienvenida);
        primaryStage.show();
    }
}