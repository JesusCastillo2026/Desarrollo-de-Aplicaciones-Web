package com.tecsup.pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponse(
        Long id,
        LocalDate fecha,
        String estado,
        BigDecimal total,
        ClienteResponse cliente,
        List<DetalleResponse> detalles) {
    public record DetalleResponse(Long id, Integer cantidad, ProductoResponse producto) {}
}
