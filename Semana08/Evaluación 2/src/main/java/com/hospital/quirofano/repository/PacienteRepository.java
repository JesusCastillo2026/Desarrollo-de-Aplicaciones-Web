package com.hospital.quirofano.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.quirofano.entity.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}