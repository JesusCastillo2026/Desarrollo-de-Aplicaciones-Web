package com.hospital.quirofano.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.hospital.quirofano.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);
    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findWithRolByNombreUsuarioIgnoreCase(String nombreUsuario);
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Long idUsuario);
    boolean existsByRol_IdRolAndActivoTrue(Long rolId);
    long countByRol_NombreRolAndActivoTrue(String nombreRol);
}
