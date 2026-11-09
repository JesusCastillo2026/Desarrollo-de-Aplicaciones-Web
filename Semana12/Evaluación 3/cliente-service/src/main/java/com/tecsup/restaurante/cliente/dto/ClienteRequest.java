package com.tecsup.restaurante.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 80) String nombres,
        @NotBlank @Size(max = 80) String apellidos,
        @NotBlank @Size(max = 20) String numeroDocumento,
        @NotBlank @Email @Size(max = 120) String correo,
        Boolean activo) {}
