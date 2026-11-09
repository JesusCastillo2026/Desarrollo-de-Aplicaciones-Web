package com.hospital.quirofano.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.AprobacionCirugia;

public interface AprobacionCirugiaRepository extends JpaRepository<AprobacionCirugia, Long> {
    List<AprobacionCirugia> findBySolicitud_IdSolicitudOrderByFechaDecisionDesc(Long solicitudId);
    Optional<AprobacionCirugia> findTopBySolicitud_IdSolicitudOrderByFechaDecisionDesc(Long solicitudId);
}
