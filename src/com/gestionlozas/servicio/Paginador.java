
package com.gestionlozas.servicio;

import java.util.ArrayList;
import java.util.List;

public class Paginador<T> {
    private List<T> datos;
    private final int tamanoPagina;
    private int paginaActual;

    public Paginador(List<T> datos, int tamanoPagina) {
        this.datos = datos != null ? datos : new ArrayList<>();
        this.tamanoPagina = tamanoPagina > 0 ? tamanoPagina : 5;
        this.paginaActual = 1;
    }

    public void actualizarDatos(List<T> nuevosDatos) {
        this.datos = nuevosDatos != null ? nuevosDatos : new ArrayList<>();
        if (paginaActual > getTotalPaginas()) {
            paginaActual = Math.max(1, getTotalPaginas());
        }
    }

    public List<T> getPaginaActualDatos() {
        if (datos.isEmpty()) return new ArrayList<>();
        int inicio = (paginaActual - 1) * tamanoPagina;
        int fin = Math.min(inicio + tamanoPagina, datos.size());
        if (inicio >= datos.size()) return new ArrayList<>();
        return datos.subList(inicio, fin);
    }

    public int getTotalPaginas() {
        if (datos.isEmpty()) return 1;
        return (int) Math.ceil((double) datos.size() / tamanoPagina);
    }

    public int getPaginaActual() { return paginaActual; }

    public void siguientePagina() {
        if (paginaActual < getTotalPaginas()) paginaActual++;
    }

    public void anteriorPagina() {
        if (paginaActual > 1) paginaActual--;
    }
}


