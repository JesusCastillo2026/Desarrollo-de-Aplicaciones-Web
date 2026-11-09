package com.tecsup.categoria.controller;
import com.tecsup.categoria.dto.CategoriaRequest;
import com.tecsup.categoria.model.Categoria;
import com.tecsup.categoria.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/categorias")
public class CategoriaController {
 private final CategoriaService service;
 public CategoriaController(CategoriaService service) { this.service=service; }
 @GetMapping public List<Categoria> listar() { return service.listar(); }
 @GetMapping("/{id}") public Categoria obtener(@PathVariable Long id) { return service.obtener(id); }
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Categoria crear(@Valid @RequestBody CategoriaRequest dto) { return service.crear(dto); }
 @PutMapping("/{id}") public Categoria actualizar(@PathVariable Long id,@Valid @RequestBody CategoriaRequest dto) { return service.actualizar(id,dto); }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
