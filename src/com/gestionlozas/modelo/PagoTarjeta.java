
package com.gestionlozas.modelo;

public class PagoTarjeta extends Pago {
    private final String tipoTarjeta;

    public PagoTarjeta(int idPago, double monto, String comprobante, String tipoTarjeta) {
        super(idPago, monto, comprobante);
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public String obtenerDetalleMetodo() { return "Tarjeta (" + tipoTarjeta + ")"; }
}
