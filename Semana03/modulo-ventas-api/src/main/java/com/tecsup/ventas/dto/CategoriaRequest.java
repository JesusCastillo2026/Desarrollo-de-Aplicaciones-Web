package com.tecsup.ventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(@NotBlank @Size(max=80) String nombre, @Size(max=255) String descripcion) {}
