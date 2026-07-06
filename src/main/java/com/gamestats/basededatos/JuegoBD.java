package com.gamestats.basededatos;

import com.gamestats.excepciones.ErrorBaseDatosException;
import com.gamestats.modelo.EstadoJuego;
import com.gamestats.modelo.Juego;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class JuegoBD {

    public void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS juegos (" +
                "id INTEGER PRIMARY KEY, " +
                "nombre TEXT, " +
                "portada TEXT, " +
                "calificacion REAL," +
                "estado TEXT," +
                "tiempo_jugado INTEGER DEFAULT 0," +
                "favorito INTEGER DEFAULT 0" +
                ")";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardar(Juego juego, EstadoJuego estado) {
        String sql = "INSERT OR IGNORE INTO juegos (id, nombre, portada, calificacion, estado, tiempo_jugado, favorito) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, juego.getId());
            ps.setString(2, juego.getName());
            ps.setString(3, juego.getBackground_image());
            ps.setDouble(4, juego.getRating());
            ps.setString(5, estado.name());
            ps.setInt(6, 0);
            ps.setBoolean(7, false);
            ps.executeUpdate();
            System.out.println("Juego guardado: " + juego.getName());

        } catch (SQLException e) {
            throw new ErrorBaseDatosException("Error al intentar guardar el juego en la base de datos", e);
        }
    }

    public List <Juego> obtenerTodos() {
        String sql = "SELECT * FROM juegos";
        List <Juego> juegos = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while (rs.next()) {
                Juego j = new Juego();
                j.setId(rs.getInt("id"));
                j.setName(rs.getString("nombre"));
                j.setBackground_image(rs.getString("portada"));
                j.setRating(rs.getDouble("calificacion"));
                j.setEstado(EstadoJuego.valueOf(rs.getString("estado").toUpperCase()));
                j.setTiempoJugadoMinutos(rs.getInt("tiempo_jugado"));
                j.setFavorito(rs.getBoolean("favorito"));
                juegos.add(j);
            }

        } catch (SQLException e) {
            throw new ErrorBaseDatosException("Error al intentar guardar el juego en la base de datos", e);
        }
        return juegos;
    }

    public void eliminarJuego(int id) {
        String sql = "DELETE FROM juegos WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Juego eliminado");

        } catch (SQLException e) {
            throw new ErrorBaseDatosException("Error al intentar guardar el juego en la base de datos", e);
        }
    }

    public void actualizarEstadoJuego(EstadoJuego estado, int id) {
        String sql = "UPDATE juegos SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado.name());
            ps.setInt(2,id);
            ps.executeUpdate();
            System.out.println("Estado actualizado");

        } catch (SQLException e) {
            throw new ErrorBaseDatosException("Error al intentar guardar el juego en la base de datos", e);
        }
    }

    // [INTERFAZ V1_3] IMPLEMENTACIÓN DE SISTEMA DE HORAS JUGADAS
    public void sumarTiempoJugado(int id, int minutosNuevos) {
        String sql = "UPDATE juegos SET tiempo_jugado = tiempo_jugado + ? WHERE id = ?";
        try (Connection conn = ConexionBD.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minutosNuevos);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorBaseDatosException("Error al actualizar tiempo", e);
        }
    }

    // [INTERFAZ V1_4] + METODO PARA ALTERNAR ESTADO DE FAVORITO
    public void actualizarFavorito(int id, boolean esFavorito) {

        String sql = "UPDATE juegos SET favorito = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, esFavorito);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw  new ErrorBaseDatosException("Error al actualizar favoritos", e);
        }
    }
}