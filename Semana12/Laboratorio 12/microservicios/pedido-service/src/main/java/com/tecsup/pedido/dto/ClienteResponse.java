package com.tecsup.pedido.dto;

public record ClienteResponse(Long id, String nombre, String apellidos, String direccion, String estado, String nroDni) {}
