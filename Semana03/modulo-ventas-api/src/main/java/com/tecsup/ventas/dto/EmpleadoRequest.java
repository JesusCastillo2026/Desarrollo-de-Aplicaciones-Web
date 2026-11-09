package com.tecsup.ventas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmpleadoRequest(
        @NotBlank @Size(max=80) String nombres,
        @NotBlank @Size(max=80) String apellidos,
        @NotBlank @Email @Size(max=120) String correo,
        @NotBlank @Size(max=80) String cargo) {}
