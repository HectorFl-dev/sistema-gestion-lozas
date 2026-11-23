
package com.gestionlozas.modelo;

import java.time.LocalDate;

public abstract class Pago {
    protected int idPago;
    protected LocalDate fechaPago;
    protected double monto;
    protected String comprobante;

    public Pago(int idPago, double monto, String comprobante) {
        this.idPago = idPago;
        this.fechaPago = LocalDate.now();
        this.monto = monto;
        this.comprobante = comprobante;
    }

    public abstract String obtenerDetalleMetodo();
    public double getMonto() { return monto; }
    public String getComprobante() { return comprobante; }
}


