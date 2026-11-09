package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.CategoriaRequest;
import com.tecsup.ventas.model.Categoria;
import com.tecsup.ventas.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service=service; }
    @GetMapping public List<Categoria> listar() { return service.listar(); }
    @GetMapping("/{id}") public Categoria obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Categoria crear(@Valid @RequestBody CategoriaRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest r) { return service.actualizar(id,r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
