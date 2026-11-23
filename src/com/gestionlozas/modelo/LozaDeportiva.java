
package com.gestionlozas.modelo;

public abstract class LozaDeportiva {
    private final int idLoza;
    private final String nombre;
    private final double tarifaHora;
    private String estado;

    public LozaDeportiva(int idLoza, String nombre, double tarifaHora) {
        this.idLoza = idLoza;
        this.nombre = nombre;
        this.tarifaHora = tarifaHora;
        this.estado = "Disponible";
    }

    public abstract double calcularCosto(int horas);
    public abstract String obtenerTipo();

    public int getIdLoza() { return idLoza; }
    public String getNombre() { return nombre; }
    public double getTarifaHora() { return tarifaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return idLoza + " - " + nombre + " (" + obtenerTipo() + ")";
    }
}

