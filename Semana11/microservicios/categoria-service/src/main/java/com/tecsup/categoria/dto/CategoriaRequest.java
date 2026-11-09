package com.tecsup.categoria.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CategoriaRequest(
        @NotBlank @Size(max=120) String nombre,
        @NotBlank @Pattern(regexp="ACTIVO|INACTIVO") String estado
) {}
