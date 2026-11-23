
package com.gestionlozas.modelo;

public class PagoDigital extends Pago {
    private final String billetera; // Yape / Plin

    public PagoDigital(int idPago, double monto, String comprobante, String billetera) {
        super(idPago, monto, comprobante);
        this.billetera = billetera;
    }

    @Override
    public String obtenerDetalleMetodo() { return billetera; }
}

