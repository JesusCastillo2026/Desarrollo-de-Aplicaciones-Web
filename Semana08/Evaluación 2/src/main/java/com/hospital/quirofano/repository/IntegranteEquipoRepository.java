package com.hospital.quirofano.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.IntegranteEquipo;

public interface IntegranteEquipoRepository extends JpaRepository<IntegranteEquipo, Long> {
    List<IntegranteEquipo> findByProgramacion_IdProgramacion(Long programacionId);
    void deleteByProgramacion_IdProgramacion(Long programacionId);
}
