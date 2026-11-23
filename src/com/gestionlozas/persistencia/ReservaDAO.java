package com.gestionlozas.persistencia;

import com.gestionlozas.modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ReservaDAO {

    public Map<Integer, LozaDeportiva> listarLozas() {
        Map<Integer, LozaDeportiva> mapa = new HashMap<>();
        String sql = "SELECT * FROM lozas";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_loza");
                String nom = rs.getString("nombre");
                double tarifa = rs.getDouble("tarifa_hora");
                String deporte = rs.getString("tipo_deporte");
                String espec = rs.getString("especificacion");

                LozaDeportiva l = "FUTBOL".equalsIgnoreCase(deporte)
                        ? new LozaFutbol(id, nom, tarifa, espec)
                        : new LozaVoley(id, nom, tarifa, espec);
                mapa.put(id, l);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar lozas: " + e.getMessage());
        }
        return mapa;
    }

    public List<Reserva> listarReservas(Map<String, Cliente> mapaClientes, Map<Integer, LozaDeportiva> catalogoLozas) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.dni FROM reservas r INNER JOIN clientes c ON r.id_cliente = c.id_cliente";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idRes = rs.getInt("id_reserva");
                String dniCli = rs.getString("dni");
                int idLoza = rs.getInt("id_loza");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                LocalTime ini = rs.getTime("hora_inicio").toLocalTime();
                LocalTime fin = rs.getTime("hora_fin").toLocalTime();
                String estado = rs.getString("estado");
                String metodo = rs.getString("metodo_pago");

                Cliente c = mapaClientes.get(dniCli);
                LozaDeportiva l = catalogoLozas.get(idLoza);
                if (c != null && l != null) {
                    Reserva res = new Reserva(idRes, c, l, fecha, ini, fin, metodo);
                    res.setEstado(estado);
                    lista.add(res);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar reservas: " + e.getMessage());
        }
        return lista;
    }

    public Reserva insertar(Cliente c, LozaDeportiva l, LocalDate fecha, LocalTime ini, LocalTime fin, String metodo) {
        // Creamos el objeto temporal para calcular monto y generar comprobante
        Reserva temp = new Reserva(0, c, l, fecha, ini, fin, metodo);
        String sql = "INSERT INTO reservas (id_cliente, id_loza, fecha, hora_inicio, hora_fin, estado, monto_total, metodo_pago, comprobante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getIdCliente());
            ps.setInt(2, l.getIdLoza());
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.setTime(4, java.sql.Time.valueOf(ini));
            ps.setTime(5, java.sql.Time.valueOf(fin));
            ps.setString(6, "Confirmada");
            ps.setDouble(7, temp.getMontoTotal());
            ps.setString(8, metodo);
            ps.setString(9, temp.getPago().getComprobante());

            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int idGenerado = keys.getInt(1);
                        return new Reserva(idGenerado, c, l, fecha, ini, fin, metodo);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al registrar reserva: " + e.getMessage());
        }
        return null;
    }

    public boolean cancelarEnBD(int idReserva) {
        String sql = "UPDATE reservas SET estado = 'Cancelada' WHERE id_reserva = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al cancelar reserva: " + e.getMessage());
        }
        return false;
    }
}

