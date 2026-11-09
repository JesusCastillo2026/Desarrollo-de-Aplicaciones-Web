package com.tecsup.restaurante.pedido.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoRequest(
        @NotNull @Positive Long idCliente,
        @Size(max = 20) String estado,
        @NotEmpty List<@Valid DetallePedidoRequest> detalles) {}
