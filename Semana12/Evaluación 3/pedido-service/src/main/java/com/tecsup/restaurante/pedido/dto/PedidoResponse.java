package com.tecsup.restaurante.pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.tecsup.restaurante.pedido.model.Pedido;

public record PedidoResponse(Long idPedido, LocalDateTime fecha, Long idCliente, BigDecimal total,
        String estado, List<DetallePedidoResponse> detalles) {
    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(pedido.getIdPedido(), pedido.getFecha(), pedido.getIdCliente(), pedido.getTotal(),
                pedido.getEstado(), pedido.getDetalles().stream().map(DetallePedidoResponse::from).toList());
    }
}
