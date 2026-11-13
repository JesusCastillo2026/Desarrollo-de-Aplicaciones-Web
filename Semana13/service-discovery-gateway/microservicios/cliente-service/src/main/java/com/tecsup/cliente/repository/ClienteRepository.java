package com.tecsup.cliente.repository;
import com.tecsup.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente,Long> {}
