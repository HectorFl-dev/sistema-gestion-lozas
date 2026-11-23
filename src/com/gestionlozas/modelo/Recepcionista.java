
package com.gestionlozas.modelo;

public class Recepcionista extends Usuario {
    private final String turno;

    public Recepcionista(int id, String dni, String nom, String ape, String tel, String email, String user, String pass, String turno) {
        super(id, dni, nom, ape, tel, email, user, pass);
        this.turno = turno;
    }

    @Override
    public String getRol() { return "Recepcionista (" + turno + ")"; }
}
