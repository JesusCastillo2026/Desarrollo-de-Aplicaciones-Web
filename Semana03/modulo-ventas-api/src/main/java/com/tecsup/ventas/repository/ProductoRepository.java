package com.tecsup.ventas.repository;

import com.tecsup.ventas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {}
