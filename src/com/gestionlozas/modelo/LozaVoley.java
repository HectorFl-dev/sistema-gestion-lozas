
package com.gestionlozas.modelo;

public class LozaVoley extends LozaDeportiva {
    private final String tipoPiso;

    public LozaVoley(int id, String nombre, double tarifa, String tipoPiso) {
        super(id, nombre, tarifa);
        this.tipoPiso = tipoPiso;
    }

    @Override
    public double calcularCosto(int horas) {
        return getTarifaHora() * horas;
    }

    @Override
    public String obtenerTipo() { return "Vóley " + tipoPiso; }
}
