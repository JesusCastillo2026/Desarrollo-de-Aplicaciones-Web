package com.tecsup.restaurante.pedido.dto;

import java.math.BigDecimal;

import com.tecsup.restaurante.pedido.model.DetallePedido;

public record DetallePedidoResponse(Long idDetalle, Long idPlato, String nombrePlato, Integer cantidad,
        BigDecimal precioUnitario, BigDecimal subtotal) {
    public static DetallePedidoResponse from(DetallePedido detalle) {
        return new DetallePedidoResponse(detalle.getIdDetalle(), detalle.getIdPlato(), detalle.getNombrePlato(),
                detalle.getCantidad(), detalle.getPrecioUnitario(), detalle.getSubtotal());
    }
}
