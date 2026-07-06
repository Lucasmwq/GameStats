package com.gamestats.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JuegoTest {

    Juego juegoBase;

    @BeforeEach
    public void setUp() {
        juegoBase = new Juego(1, "Test Game", "url.jpg", 4.0);
    }

    @Test
    public void testInstanciacionJuego() {
        assertEquals(1, juegoBase.getId());
        assertEquals("Test Game", juegoBase.getName());
        assertEquals(4.0, juegoBase.getRating());
    }

    @Test
    public void testEstadoPorDefecto() {
        assertEquals(EstadoJuego.PENDIENTE, juegoBase.getEstado());
    }

    @Test
    public void testAlternarFavoritos() {
        assertFalse(juegoBase.isFavorito());
        juegoBase.setFavorito(true);
        assertTrue(juegoBase.isFavorito());
    }

    @Test
    public void testConversionTiempoJugado() {
        juegoBase.setTiempoJugado(90);
        assertEquals(1.5, juegoBase.getTiempoJugadoDecimal(), 0.01);
    }

    @Test
    public void testEnumEstadoJuego() {
        juegoBase.setEstado(EstadoJuego.COMPLETADO);
        assertEquals(EstadoJuego.COMPLETADO, juegoBase.getEstado());

        juegoBase.setEstado(EstadoJuego.JUGANDO);
        assertEquals(EstadoJuego.JUGANDO, juegoBase.getEstado());
    }
}