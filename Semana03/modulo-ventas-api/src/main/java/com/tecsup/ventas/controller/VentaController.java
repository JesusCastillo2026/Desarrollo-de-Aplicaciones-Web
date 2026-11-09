package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.VentaRequest;
import com.tecsup.ventas.model.Venta;
import com.tecsup.ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService service;
    public VentaController(VentaService service) { this.service=service; }
    @GetMapping public List<Venta> listar() { return service.listar(); }
    @GetMapping("/{id}") public Venta obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Venta crear(@Valid @RequestBody VentaRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public Venta actualizar(@PathVariable Long id, @Valid @RequestBody VentaRequest r) { return service.actualizar(id,r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
