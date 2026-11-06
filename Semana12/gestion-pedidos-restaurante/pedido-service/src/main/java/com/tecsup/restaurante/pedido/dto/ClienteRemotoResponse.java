package com.tecsup.restaurante.pedido.dto;

public record ClienteRemotoResponse(Long idCliente, String nombres, String apellidos, String numeroDocumento, String correo, boolean activo) {}
