package com.tecsup.ventas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record VentaRequest(
        @NotNull Long clienteId,
        @NotNull Long empleadoId,
        @Pattern(regexp="REGISTRADA|PAGADA|ANULADA") String estado,
        @NotEmpty List<@Valid DetalleVentaRequest> detalles) {}
