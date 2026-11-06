package com.tecsup.restaurante.plato.controller;

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

import com.tecsup.restaurante.plato.dto.PlatoRequest;
import com.tecsup.restaurante.plato.dto.PlatoResponse;
import com.tecsup.restaurante.plato.services.PlatoService;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {
    private final PlatoService service;

    public PlatoController(PlatoService service) { this.service = service; }

    @GetMapping
    public List<PlatoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public PlatoResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping
    public ResponseEntity<PlatoResponse> crear(@Valid @RequestBody PlatoRequest request) {
        PlatoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/platos/" + creado.idPlato())).body(creado);
    }

    @PutMapping("/{id}")
    public PlatoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PlatoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
