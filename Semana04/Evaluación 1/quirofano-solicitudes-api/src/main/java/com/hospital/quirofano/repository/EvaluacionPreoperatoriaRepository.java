package com.hospital.quirofano.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.EvaluacionPreoperatoria;

public interface EvaluacionPreoperatoriaRepository extends JpaRepository<EvaluacionPreoperatoria, Long> {
    List<EvaluacionPreoperatoria> findBySolicitud_IdSolicitudOrderByFechaEvaluacionDesc(Long solicitudId);
    List<EvaluacionPreoperatoria> findBySolicitud_Paciente_IdPacienteOrderByFechaEvaluacionDesc(Long pacienteId);
    java.util.Optional<EvaluacionPreoperatoria> findTopBySolicitud_IdSolicitudOrderByFechaEvaluacionDesc(Long solicitudId);
}
