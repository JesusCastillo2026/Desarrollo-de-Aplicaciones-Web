package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.ProductoRequest;
import com.tecsup.ventas.model.Producto;
import com.tecsup.ventas.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service=service; }
    @GetMapping public List<Producto> listar() { return service.listar(); }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Producto crear(@Valid @RequestBody ProductoRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest r) { return service.actualizar(id,r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
