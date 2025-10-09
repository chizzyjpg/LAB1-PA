// src/main/java/dao/UsuarioDAO.java
package dao;

import BD.CConexion;

import java.sql.*;

public class UsuarioDAO {

    public boolean existeNickname(String nickname) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE nickname = ? LIMIT 1";
        try (Connection c = CConexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nickname);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE email = ? LIMIT 1";
        try (Connection c = CConexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public long crearUsuario(String nickname, String nombre, String email,
                             String passwordHash, String imagenPath, String tipo) throws SQLException {
        String sql = """
            INSERT INTO usuario (nickname, nombre, email, password_hash, imagen_path, tipo, creado_en)
            VALUES (?, ?, ?, ?, ?, ?, now())
            RETURNING id
            """;
        try (Connection c = CConexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nickname);
            ps.setString(2, nombre);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.setString(5, imagenPath);
            ps.setString(6, tipo);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void crearCliente(long usuarioId, String apellido, Date fechaNac,
                             String nacionalidad, String tipoDoc, String numDoc) throws SQLException {
        String sql = """
            INSERT INTO cliente (usuario_id, apellido, fecha_nac, nacionalidad, tipo_doc, num_doc)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = CConexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
            ps.setString(2, apellido);
            ps.setDate(3, fechaNac);
            ps.setString(4, nacionalidad);
            ps.setString(5, tipoDoc);
            ps.setString(6, numDoc);
            ps.executeUpdate();
        }
    }

    public void crearAerolinea(long usuarioId, String descripcion, String sitioWeb) throws SQLException {
        String sql = "INSERT INTO aerolinea (usuario_id, descripcion, sitio_web) VALUES (?,?,?)";
        try (Connection c = CConexion.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
            ps.setString(2, descripcion);
            if (sitioWeb == null || sitioWeb.isBlank()) ps.setNull(3, Types.VARCHAR);
            else ps.setString(3, sitioWeb);
            ps.executeUpdate();
        }
    }
}
