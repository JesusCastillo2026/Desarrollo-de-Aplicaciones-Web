package com.tecsup.restaurante.pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetallePedidoRequest(@NotNull @Positive Long idPlato, @NotNull @Positive Integer cantidad) {}
