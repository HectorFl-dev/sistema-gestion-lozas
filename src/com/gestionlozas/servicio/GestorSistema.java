package com.gestionlozas.servicio;

import com.gestionlozas.modelo.*;
import com.gestionlozas.persistencia.ClienteDAO;
import com.gestionlozas.persistencia.ReservaDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class GestorSistema {

    private final ClienteDAO clienteDAO;
    private final ReservaDAO reservaDAO;

    private final Map<String, Cliente> mapaClientes;
    private final List<Reserva> listaReservas;
    private final Map<Integer, LozaDeportiva> catalogoLozas;

    public GestorSistema() {
        this.clienteDAO = new ClienteDAO();
        this.reservaDAO = new ReservaDAO();

        // Carga real desde MySQL a las colecciones en memoria
        this.catalogoLozas = reservaDAO.listarLozas();
        this.mapaClientes = clienteDAO.listarTodosMap();
        this.listaReservas = reservaDAO.listarReservas(mapaClientes, catalogoLozas);
        GestorArchivosTXT.guardarClientes(mapaClientes.values());
        GestorArchivosTXT.guardarReservas(listaReservas);
    }

    public Usuario iniciarSesion(String user, String pass) {
        return clienteDAO.autenticarUsuario(user, pass);
    }

    // --- CRUD CLIENTES (MySQL + HashMap) ---
    public boolean registrarCliente(String dni, String nom, String ape, String tel, String email) {
        if (mapaClientes.containsKey(dni)) return false;
        Cliente nuevo = clienteDAO.insertar(dni, nom, ape, tel, email);
        if (nuevo != null) {
            mapaClientes.put(dni, nuevo);
            GestorArchivosTXT.guardarClientes(mapaClientes.values());
            return true;
        }
        return false;
    }

    public Cliente buscarClientePorDni(String dni) {
        return mapaClientes.get(dni);
    }

    public boolean actualizarCliente(String dni, String nom, String ape, String tel, String email) {
        Cliente c = mapaClientes.get(dni);
        if (c != null && clienteDAO.actualizar(dni, nom, ape, tel, email)) {
            c.setNombre(nom);
            c.setApellido(ape);
            c.setTelefono(tel);
            c.setEmail(email);
            GestorArchivosTXT.guardarClientes(mapaClientes.values());
            return true;
        }
        return false;
    }

    public boolean eliminarCliente(String dni) {
        if (mapaClientes.containsKey(dni) && clienteDAO.eliminar(dni)) {
            mapaClientes.remove(dni);
            GestorArchivosTXT.guardarClientes(mapaClientes.values());
            return true;
        }
        return false;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(mapaClientes.values());
    }

    public Collection<LozaDeportiva> listarLozas() {
        return catalogoLozas.values();
    }

    public LozaDeportiva buscarLozaPorId(int id) {
        return catalogoLozas.get(id);
    }

    // --- CRUD RESERVAS (MySQL + ArrayList + Iterator + Comparator) ---
    public boolean verificarDisponibilidad(LozaDeportiva loza, LocalDate fecha, LocalTime ini, LocalTime fin) {
        for (Reserva r : listaReservas) {
            if (r.getLoza().getIdLoza() == loza.getIdLoza()
                    && r.getFecha().equals(fecha)
                    && r.getEstado().equalsIgnoreCase("Confirmada")) {
                if (ini.isBefore(r.getHoraFin()) && fin.isAfter(r.getHoraInicio())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Reserva registrarReserva(String dniCliente, int idLoza, LocalDate fecha, LocalTime ini, LocalTime fin, String metodo) {
        Cliente c = buscarClientePorDni(dniCliente);
        LozaDeportiva l = buscarLozaPorId(idLoza);
        if (c == null || l == null) return null;
        if (!verificarDisponibilidad(l, fecha, ini, fin)) return null;

        Reserva res = reservaDAO.insertar(c, l, fecha, ini, fin, metodo);
        if (res != null) {
            listaReservas.add(res);
            GestorArchivosTXT.guardarReservas(listaReservas);
        }
        return res;
    }

    public Reserva buscarReservaPorId(int id) {
        for (Reserva r : listaReservas) {
            if (r.getIdReserva() == id) return r;
        }
        return null;
    }

    public boolean cancelarReservaConIterator(int idReserva) {
        Iterator<Reserva> it = listaReservas.iterator();
        while (it.hasNext()) {
            Reserva r = it.next();
            if (r.getIdReserva() == idReserva) {
                if (reservaDAO.cancelarEnBD(idReserva)) {
                    r.setEstado("Cancelada");
                    GestorArchivosTXT.guardarReservas(listaReservas);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Reserva> ordenarReservasPorFecha() {
        List<Reserva> lista = new ArrayList<>(listaReservas);
        lista.sort(Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicio));
        return lista;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }
}


