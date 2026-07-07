package com.gamestats.basededatos;

import com.gamestats.modelo.EstadoJuego;
import com.gamestats.modelo.Juego;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JuegoBDTest {

    private JuegoBD db;

    @BeforeEach
    public void setUp() {
        db = new JuegoBD();
    }

    @AfterEach
    public void limpiar() {
        // Limpiamos el juego de prueba por si algún test falló a la mitad
        assertDoesNotThrow(() -> db.eliminarJuego(-999));
    }

    @Test
    public void testCreacionEstructuraBD() {
        assertDoesNotThrow(() -> db.crearTabla());
    }

    @Test
    public void testGuardarEnBD() {
        Juego juegoPrueba = new Juego(-999, "Juego Unitario", "img.jpg", 1.0);
        assertDoesNotThrow(() -> db.guardar(juegoPrueba, EstadoJuego.PENDIENTE));
    }

    @Test
    public void testObtenerTodosNoEsNulo() {
        List<Juego> coleccion = db.obtenerTodos();
        assertNotNull(coleccion);
    }
}