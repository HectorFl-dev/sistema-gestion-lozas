
package com.gestionlozas.modelo;

public class LozaFutbol extends LozaDeportiva {
    private final String tipoCesped;

    public LozaFutbol(int id, String nombre, double tarifa, String tipoCesped) {
        super(id, nombre, tarifa);
        this.tipoCesped = tipoCesped;
    }

    @Override
    public double calcularCosto(int horas) {
        double recargo = tipoCesped.equalsIgnoreCase("Sintético") ? 10.0 : 5.0;
        return (getTarifaHora() + recargo) * horas;
    }

    @Override
    public String obtenerTipo() { return "Fútbol " + tipoCesped; }
}

