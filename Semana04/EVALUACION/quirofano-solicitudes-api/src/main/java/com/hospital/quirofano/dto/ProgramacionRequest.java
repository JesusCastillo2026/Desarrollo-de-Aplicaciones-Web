package com.hospital.quirofano.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProgramacionRequest(
        Long solicitudId,
        Long quirofanoId,
        Long cirujanoId,
        Long anestesiologoId,
        LocalDateTime fechaHoraInicio,
        Integer duracionEstimadaMinutos,
        String prioridad,
        String motivoCambio,
        String responsableCambio,
        List<IntegranteRequest> equipo) {}
