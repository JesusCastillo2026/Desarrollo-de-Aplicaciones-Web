package com.tecsup.restaurante.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsup.restaurante.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByNumeroDocumentoIgnoreCase(String numeroDocumento);
    boolean existsByNumeroDocumentoIgnoreCaseAndIdClienteNot(String numeroDocumento, Long idCliente);
}
