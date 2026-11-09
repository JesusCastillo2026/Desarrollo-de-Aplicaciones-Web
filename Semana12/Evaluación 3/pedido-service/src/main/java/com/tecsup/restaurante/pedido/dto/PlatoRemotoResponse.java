package com.tecsup.restaurante.pedido.dto;

import java.math.BigDecimal;

public record PlatoRemotoResponse(Long idPlato, String codigo, String nombre, String descripcion, BigDecimal precio, boolean disponible) {}
