package com.tecsup.restaurante.plato.dto;

import java.math.BigDecimal;

import com.tecsup.restaurante.plato.model.Plato;

public record PlatoResponse(Long idPlato, String codigo, String nombre, String descripcion, BigDecimal precio, boolean disponible) {
    public static PlatoResponse from(Plato plato) {
        return new PlatoResponse(plato.getIdPlato(), plato.getCodigo(), plato.getNombre(),
                plato.getDescripcion(), plato.getPrecio(), plato.isDisponible());
    }
}
