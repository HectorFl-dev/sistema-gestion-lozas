package com.gestionlozas.modelo;

public abstract class Usuario extends Persona {
    private final int idUsuario;
    private final String username;
    private final String contrasena;
    private final boolean activo;

    public Usuario(int idUsuario, String dni, String nombre, String apellido, String telefono, String email, String username, String contrasena) {
        super(dni, nombre, apellido, telefono, email);
        this.idUsuario = idUsuario;
        this.username = username;
        this.contrasena = contrasena;
        this.activo = true;
    }

    public boolean autenticar(String pass) {
        return this.activo && this.contrasena.equals(pass);
    }

    public abstract String getRol();
    public String getUsername() { return username; }
}

