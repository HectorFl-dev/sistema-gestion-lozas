
package com.gestionlozas.modelo;

import java.time.LocalDate;

public record ResumenComprobante(
    String comprobante,
    String cliente,
    String loza,
    LocalDate fecha,
    double total,
    String metodoPago
) {}

