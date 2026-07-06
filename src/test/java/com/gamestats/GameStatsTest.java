package com.gamestats;


import com.gamestats.api.ParsearJSON;
import com.gamestats.basededatos.JuegoBD;
import com.gamestats.excepciones.RespuestaApiInvalidaException;
import com.gamestats.modelo.EstadoJuego;
import com.gamestats.modelo.Juego;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStatsTest {


    // TEST #1
    @Test
    public void testInstanciacionJuego() {
        Juego juego = new Juego(1, "Test Game", "url.jpg", 4.5);

        assertEquals(1,juego.getId());
        assertEquals("Test Game", juego.getName());
        assertEquals(4.5, juego.getRating());
    }

    //TEST #2
    @Test
    public void testEstadoPorDefecto() {
        Juego juego = new Juego(2, "Test Game 2", "url.jpg", 3.0);
        assertEquals(EstadoJuego.PENDIENTE, juego.getEstado());
    }

    // TEST #3
    @Test
    public void testAlternarFavoritos() {
        Juego juego = new Juego(3, "Fav Game", "url.jpg", 5.0);
        assertFalse(juego.isFavorito());
        juego.setFavorito(true);
        assertTrue(juego.isFavorito());
    }

    //TEST #4
    @Test
    public void testConversionTiempoJugado() {
        Juego juego = new Juego(4, "Time Game", "url.jpg", 4.0);
        juego.setTiempoJugado(90);
        assertEquals(1.5, juego.getTiempoJugadoDecimal(), 0.01);
    }

    // TEST #5
    @Test
    public void testEnumEstadoJuego() {
        Juego juego = new Juego(4, "Time Game", "url.jpg", 4.0);
        juego.setTiempoJugado(90);
        assertEquals(1.5, juego.getTiempoJugadoDecimal(), 0.01);
    }

    // PRUEBAS DE LA API (PARSEO DE JSON)

    //TEST 6
    @Test
    public void testParseoJSONValido() throws Exception {
        ParsearJSON parser = new ParsearJSON();
        String jsonSimulado = "{ \"results\": [ { \"id\": 999, \"name\": \"Juego Falso\", \"background_image\": \"img.jpg\", \"rating\": 4.8 } ] }";

        List<Juego> resultados = parser.parsearJuegos(jsonSimulado);

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Juego Falso", resultados.get(0).getName());
    }

    //TEST 7
    @Test
    public void testParseoJSONInvalidoLanzaExcepcion() {


        com.gamestats.api.ParsearJSON parser = new com.gamestats.api.ParsearJSON();
        String jsonRoto = "{ \"malformado\": [ { id: sincomillas } ] }";

        assertThrows(com.gamestats.excepciones.RespuestaApiInvalidaException.class, () -> {
            parser.parsearJuegos(jsonRoto);
        });
    }

    // PRUEBAS DE LA BASE DE DATOS

    //TEST 8
    @Test
    public void testCreacionEstructuraBD() {
        JuegoBD db = new JuegoBD();
        // Verifica que el metodo no lance SQLExeption o errores de ejecucion
        assertDoesNotThrow(() -> db.crearTabla());
    }

    //TEST 9
    @Test
    public void testGuardarYEliminarEnBD() {

        JuegoBD db = new JuegoBD();
        Juego juegoPrueba = new Juego(-999, "Juego Unitario","img.jpg", 1.0); // ID negativo para no chocar con reales

        //GUARDAR
        assertDoesNotThrow(() -> db.guardar(juegoPrueba, EstadoJuego.PENDIENTE));

        //ELIMINAR
        assertDoesNotThrow(() -> db.eliminarJuego(-999));
    }

    //TEST #10
    @Test
    public void testObtenerTodosNoEsNulo() {
        JuegoBD db = new JuegoBD();
        List<Juego> colecccion = db.obtenerTodos();
        assertNotNull(colecccion);
    }
}
