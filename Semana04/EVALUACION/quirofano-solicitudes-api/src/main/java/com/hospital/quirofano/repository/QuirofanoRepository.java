package com.hospital.quirofano.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.Quirofano;

public interface QuirofanoRepository extends JpaRepository<Quirofano, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdQuirofanoNot(String codigo, Long idQuirofano);
}
