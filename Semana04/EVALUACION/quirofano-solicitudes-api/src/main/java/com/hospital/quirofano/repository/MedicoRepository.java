package com.hospital.quirofano.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.quirofano.entity.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}