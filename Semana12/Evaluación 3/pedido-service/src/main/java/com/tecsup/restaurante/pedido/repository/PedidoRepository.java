package com.tecsup.restaurante.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsup.restaurante.pedido.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
