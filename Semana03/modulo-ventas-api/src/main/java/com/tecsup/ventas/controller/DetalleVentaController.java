package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.DetalleVentaRequest;
import com.tecsup.ventas.model.DetalleVenta;
import com.tecsup.ventas.service.DetalleVentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas/{ventaId}/detalles")
public class DetalleVentaController {
    private final DetalleVentaService service;
    public DetalleVentaController(DetalleVentaService service) { this.service=service; }
    @GetMapping public List<DetalleVenta> listar(@PathVariable Long ventaId) { return service.listar(ventaId); }
    @GetMapping("/{detalleId}") public DetalleVenta obtener(@PathVariable Long ventaId, @PathVariable Long detalleId) { return service.obtener(ventaId, detalleId); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public DetalleVenta crear(@PathVariable Long ventaId, @Valid @RequestBody DetalleVentaRequest r) { return service.crear(ventaId,r); }
    @PutMapping("/{detalleId}") public DetalleVenta actualizar(@PathVariable Long ventaId, @PathVariable Long detalleId, @Valid @RequestBody DetalleVentaRequest r) { return service.actualizar(ventaId,detalleId,r); }
    @DeleteMapping("/{detalleId}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long ventaId, @PathVariable Long detalleId) { service.eliminar(ventaId,detalleId); }
}
