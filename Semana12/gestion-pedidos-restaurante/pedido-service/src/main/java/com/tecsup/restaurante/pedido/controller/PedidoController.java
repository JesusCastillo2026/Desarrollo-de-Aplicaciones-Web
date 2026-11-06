package com.tecsup.restaurante.pedido.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.restaurante.pedido.dto.PedidoRequest;
import com.tecsup.restaurante.pedido.dto.PedidoResponse;
import com.tecsup.restaurante.pedido.services.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) { this.service = service; }

    @GetMapping
    public List<PedidoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.idPedido())).body(creado);
    }

    @PutMapping("/{id}")
    public PedidoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
