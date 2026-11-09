package com.tecsup.restaurante.plato.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlatoRequest(
        @NotBlank @Size(max = 30) String codigo,
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 500) String descripcion,
        @NotNull @DecimalMin(value = "0.01") BigDecimal precio,
        Boolean disponible) {}
