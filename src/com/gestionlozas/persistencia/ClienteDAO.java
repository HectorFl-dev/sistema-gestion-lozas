package com.gestionlozas.persistencia;

import com.gestionlozas.modelo.*;
import java.sql.*;
import java.util.*;

public class ClienteDAO {

    public Usuario autenticarUsuario(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND contrasena = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String dni = rs.getString("dni");
                    String nom = rs.getString("nombre");
                    String ape = rs.getString("apellido");
                    String tel = rs.getString("telefono");
                    String email = rs.getString("email");
                    String rol = rs.getString("rol");
                    String detalle = rs.getString("detalle_rol");

                    if ("ADMIN".equalsIgnoreCase(rol)) {
                        return new Administrador(id, dni, nom, ape, tel, email, username, password, detalle);
                    } else {
                        return new Recepcionista(id, dni, nom, ape, tel, email, username, password, detalle);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al autenticar: " + e.getMessage());
        }
        return null;
    }

    public Map<String, Cliente> listarTodosMap() {
        Map<String, Cliente> mapa = new HashMap<>();
        String sql = "SELECT * FROM clientes";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email")
                );
                mapa.put(c.getDni(), c);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar clientes: " + e.getMessage());
        }
        return mapa;
    }

    public Cliente insertar(String dni, String nom, String ape, String tel, String email) {
        String sql = "INSERT INTO clientes (dni, nombre, apellido, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dni);
            ps.setString(2, nom);
            ps.setString(3, ape);
            ps.setString(4, tel);
            ps.setString(5, email);
            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new Cliente(keys.getInt(1), dni, nom, ape, tel, email);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al insertar cliente: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(String dni, String nom, String ape, String tel, String email) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, telefono = ?, email = ? WHERE dni = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, ape);
            ps.setString(3, tel);
            ps.setString(4, email);
            ps.setString(5, dni);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar cliente: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(String dni) {
        String sql = "DELETE FROM clientes WHERE dni = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar cliente: " + e.getMessage());
        }
        return false;
    }
}

