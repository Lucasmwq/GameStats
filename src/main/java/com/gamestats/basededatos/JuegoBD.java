package com.gamestats.basededatos;

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
                "calificacion REAL" +
                ")";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabla creada correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardar(Juego juego) {
        String sql = "INSERT OR IGNORE INTO juegos (id, nombre, portada, calificacion) VALUES (?,?,?,?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, juego.getId());
            ps.setString(2, juego.getName());
            ps.setString(3, juego.getBackground_image());
            ps.setDouble(4, juego.getRating());
            ps.executeUpdate();
            System.out.println("Juego guardado: " + juego.getName());

        } catch (SQLException e) {
            e.printStackTrace();
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
                juegos.add(j);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
