package com.tecsup.restaurante.plato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsup.restaurante.plato.model.Plato;

public interface PlatoRepository extends JpaRepository<Plato, Long> {
    boolean existsByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCaseAndIdPlatoNot(String codigo, Long idPlato);
}
