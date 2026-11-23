
package com.gestionlozas.modelo;

public class PagoEfectivo extends Pago {
    public PagoEfectivo(int idPago, double monto, String comprobante) {
        super(idPago, monto, comprobante);
    }

    @Override
    public String obtenerDetalleMetodo() { return "Efectivo"; }
}

