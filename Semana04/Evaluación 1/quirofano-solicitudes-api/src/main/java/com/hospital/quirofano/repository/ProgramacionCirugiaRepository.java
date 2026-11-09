package com.hospital.quirofano.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.ProgramacionCirugia;

public interface ProgramacionCirugiaRepository extends JpaRepository<ProgramacionCirugia, Long> {
    List<ProgramacionCirugia> findBySolicitud_IdSolicitudOrderByFechaHoraInicio(Long solicitudId);
}
