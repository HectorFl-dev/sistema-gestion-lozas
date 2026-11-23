
package com.gestionlozas.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Reserva {
    private final int idReserva;
    private final Cliente cliente;
    private final LozaDeportiva loza;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private String estado;
    private final double montoTotal;
    private final Pago pago;

    public Reserva(int idReserva, Cliente cliente, LozaDeportiva loza, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String metodoPago) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.loza = loza;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "Confirmada";

        int horas = (int) ChronoUnit.HOURS.between(horaInicio, horaFin);
        if (horas <= 0) horas = 1;
        this.montoTotal = loza.calcularCosto(horas);

        String comp = "B001-" + String.format("%04d", idReserva);
        if (metodoPago.equalsIgnoreCase("Yape") || metodoPago.equalsIgnoreCase("Plin")) {
            this.pago = new PagoDigital(idReserva, this.montoTotal, comp, metodoPago);
        } else if (metodoPago.equalsIgnoreCase("Tarjeta")) {
            this.pago = new PagoTarjeta(idReserva, this.montoTotal, comp, "Crédito/Débito");
        } else {
            this.pago = new PagoEfectivo(idReserva, this.montoTotal, comp);
        }
    }

    public int getIdReserva() { return idReserva; }
    public Cliente getCliente() { return cliente; }
    public LozaDeportiva getLoza() { return loza; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getMontoTotal() { return montoTotal; }
    public Pago getPago() { return pago; }

    public ResumenComprobante generarResumenRecord() {
        return new ResumenComprobante(
            pago.getComprobante(),
            cliente.getNombreCompleto(),
            loza.getNombre(),
            fecha,
            montoTotal,
            pago.obtenerDetalleMetodo()
        );
    }
}


