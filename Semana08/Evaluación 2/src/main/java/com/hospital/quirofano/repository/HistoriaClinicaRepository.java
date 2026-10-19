package com.hospital.quirofano.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.quirofano.entity.HistoriaClinica;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
}