package com.tecsup.pedido.controller;

import com.tecsup.pedido.dto.EstadoRequest;
import com.tecsup.pedido.dto.PedidoRequest;
import com.tecsup.pedido.dto.PedidoResponse;
import com.tecsup.pedido.service.PedidoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) { this.service = service; }

    @GetMapping
    public List<PedidoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public PedidoResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(@Valid @RequestBody PedidoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    public PedidoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) {
        return service.cambiarEstado(id, request);
    }
}
