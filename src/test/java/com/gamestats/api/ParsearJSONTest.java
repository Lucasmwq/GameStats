package com.gamestats.api;

import com.gamestats.excepciones.RespuestaApiInvalidaException;
import com.gamestats.modelo.Juego;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParsearJSONTest {

    @Test
    public void testParseoJSONValido() throws Exception {
        ParsearJSON parser = new ParsearJSON();
        String jsonSimulado = "{ \"results\": [ { \"id\": 999, \"name\": \"Juego Falso\", \"background_image\": \"img.jpg\", \"rating\": 4.8 } ] }";

        List<Juego> resultados = parser.parsearJuegos(jsonSimulado);

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Juego Falso", resultados.get(0).getName());
    }

    @Test
    public void testParseoJSONInvalidoLanzaExcepcion() {
        ParsearJSON parser = new ParsearJSON();
        String jsonRoto = "{ \"malformado\": [ { id: sincomillas } ] }";

        assertThrows(RespuestaApiInvalidaException.class, () -> {
            parser.parsearJuegos(jsonRoto);
        });
    }
}