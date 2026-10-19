package com.hospital.quirofano.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.AuditoriaOperacion;

public interface AuditoriaOperacionRepository extends JpaRepository<AuditoriaOperacion, Long> {
    List<AuditoriaOperacion> findTop200ByOrderByFechaHoraDesc();
}
