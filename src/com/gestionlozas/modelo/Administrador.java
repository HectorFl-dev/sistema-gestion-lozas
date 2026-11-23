
package com.gestionlozas.modelo;

public class Administrador extends Usuario {
    private final String cargo;

    public Administrador(int id, String dni, String nom, String ape, String tel, String email, String user, String pass, String cargo) {
        super(id, dni, nom, ape, tel, email, user, pass);
        this.cargo = cargo;
    }

    @Override
    public String getRol() { return "Administrador (" + cargo + ")"; }
}

