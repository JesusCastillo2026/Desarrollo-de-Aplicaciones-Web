package com.tecsup.restaurante.cliente.dto;

import com.tecsup.restaurante.cliente.model.Cliente;

public record ClienteResponse(Long idCliente, String nombres, String apellidos, String numeroDocumento, String correo, boolean activo) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getIdCliente(), cliente.getNombres(), cliente.getApellidos(),
                cliente.getNumeroDocumento(), cliente.getCorreo(), cliente.isActivo());
    }
}
