package com.gestionlozas.servicio;

import com.gestionlozas.modelo.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class GestorArchivosTXT {

    private static final String ARCHIVO_CLIENTES = "clientes.txt";
    private static final String ARCHIVO_RESERVAS = "reservas.txt";

    // ==========================================
    // GUARDAR CLIENTES EN TXT
    // ==========================================
    public static void guardarClientes(Collection<Cliente> clientes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CLIENTES))) {
            for (Cliente c : clientes) {
                // Formato: id;dni;nombre;apellido;telefono;email
                pw.println(c.getIdCliente() + ";" +
                           c.getDni() + ";" +
                           c.getNombre() + ";" +
                           c.getApellido() + ";" +
                           c.getTelefono() + ";" +
                           c.getEmail());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar clientes.txt: " + e.getMessage());
        }
    }

    // ==========================================
    // CARGAR CLIENTES DESDE TXT
    // ==========================================
    public static Map<String, Cliente> cargarClientes() {
        Map<String, Cliente> mapa = new HashMap<>();
        File archivo = new File(ARCHIVO_CLIENTES);
        if (!archivo.exists()) return mapa;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                if (datos.length >= 6) {
                    int id = Integer.parseInt(datos[0]);
                    String dni = datos[1];
                    String nom = datos[2];
                    String ape = datos[3];
                    String tel = datos[4];
                    String email = datos[5];
                    mapa.put(dni, new Cliente(id, dni, nom, ape, tel, email));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar clientes.txt: " + e.getMessage());
        }
        return mapa;
    }

    // ==========================================
    // GUARDAR RESERVAS EN TXT
    // ==========================================
    public static void guardarReservas(List<Reserva> reservas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_RESERVAS))) {
            for (Reserva r : reservas) {
                // Formato: idReserva;dniCliente;idLoza;fecha;horaInicio;horaFin;estado;metodoPago
                pw.println(r.getIdReserva() + ";" +
                           r.getCliente().getDni() + ";" +
                           r.getLoza().getIdLoza() + ";" +
                           r.getFecha().toString() + ";" +
                           r.getHoraInicio().toString() + ";" +
                           r.getHoraFin().toString() + ";" +
                           r.getEstado() + ";" +
                           r.getPago().obtenerDetalleMetodo());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar reservas.txt: " + e.getMessage());
        }
    }

    // ==========================================
    // CARGAR RESERVAS DESDE TXT
    // ==========================================
    public static List<Reserva> cargarReservas(Map<String, Cliente> clientes, Map<Integer, LozaDeportiva> lozas) {
        List<Reserva> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_RESERVAS);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                if (datos.length >= 8) {
                    int idRes = Integer.parseInt(datos[0]);
                    String dniCli = datos[1];
                    int idLoza = Integer.parseInt(datos[2]);
                    LocalDate fecha = LocalDate.parse(datos[3]);
                    LocalTime ini = LocalTime.parse(datos[4]);
                    LocalTime fin = LocalTime.parse(datos[5]);
                    String estado = datos[6];
                    String metodo = datos[7];

                    Cliente c = clientes.get(dniCli);
                    LozaDeportiva l = lozas.get(idLoza);

                    if (c != null && l != null) {
                        Reserva r = new Reserva(idRes, c, l, fecha, ini, fin, metodo);
                        r.setEstado(estado);
                        lista.add(r);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar reservas.txt: " + e.getMessage());
        }
        return lista;
    }
}

