
package com.gestionlozas.modelo;

public class Cliente extends Persona {
    private final int idCliente;

    public Cliente(int idCliente, String dni, String nombre, String apellido, String telefono, String email) {
        super(dni, nombre, apellido, telefono, email);
        this.idCliente = idCliente;
    }

    public int getIdCliente() { return idCliente; }

    @Override
    public String toString() {
        return getNombreCompleto() + " [DNI: " + dni + "]";
    }
}


