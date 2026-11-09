package com.tecsup.pedido.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public record PedidoRequest(
        @NotNull LocalDate fecha,
        @NotNull @Positive Long idCliente,
        @NotEmpty List<@Valid DetalleRequest> detalles) {
    public record DetalleRequest(@NotNull @Positive Long idProducto, @NotNull @Positive Integer cantidad) {}
}
