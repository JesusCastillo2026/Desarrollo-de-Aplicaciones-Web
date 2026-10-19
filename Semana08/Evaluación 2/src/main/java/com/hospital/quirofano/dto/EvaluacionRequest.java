package com.hospital.quirofano.dto;

public record EvaluacionRequest(
        Long solicitudId,
        Long profesionalId,
        String antecedentesRelevantes,
        String alergias,
        String medicacionActual,
        String observaciones,
        String estado) {}
