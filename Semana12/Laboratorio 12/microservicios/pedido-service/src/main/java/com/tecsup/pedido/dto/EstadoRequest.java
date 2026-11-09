package com.tecsup.pedido.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record EstadoRequest(@NotBlank @Pattern(regexp = "PENDIENTE|ANULADO|ATENDIDO") String estado) {}
