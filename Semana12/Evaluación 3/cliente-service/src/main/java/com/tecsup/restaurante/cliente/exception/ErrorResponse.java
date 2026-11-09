package com.tecsup.restaurante.cliente.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(OffsetDateTime fechaHora, int estado, String error, String mensaje, String ruta) {}
