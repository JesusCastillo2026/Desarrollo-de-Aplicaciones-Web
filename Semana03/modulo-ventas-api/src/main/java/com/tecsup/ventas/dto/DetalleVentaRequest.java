package com.tecsup.ventas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequest(@NotNull Long productoId, @NotNull @Positive Integer cantidad) {}
