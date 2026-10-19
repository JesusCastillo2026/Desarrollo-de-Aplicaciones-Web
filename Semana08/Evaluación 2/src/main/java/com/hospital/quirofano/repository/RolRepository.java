package com.hospital.quirofano.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.quirofano.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);
    boolean existsByNombreRolIgnoreCase(String nombreRol);
}
