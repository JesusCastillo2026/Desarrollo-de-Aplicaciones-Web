package com.tecsup.paciente.repository;
import com.tecsup.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PacienteRepository extends JpaRepository<Paciente,Long> {}
