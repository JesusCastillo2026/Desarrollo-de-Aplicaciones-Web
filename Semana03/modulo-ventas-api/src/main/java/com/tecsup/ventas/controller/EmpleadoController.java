package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.EmpleadoRequest;
import com.tecsup.ventas.model.Empleado;
import com.tecsup.ventas.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private final EmpleadoService service;
    public EmpleadoController(EmpleadoService service) { this.service=service; }
    @GetMapping public List<Empleado> listar() { return service.listar(); }
    @GetMapping("/{id}") public Empleado obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Empleado crear(@Valid @RequestBody EmpleadoRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public Empleado actualizar(@PathVariable Long id, @Valid @RequestBody EmpleadoRequest r) { return service.actualizar(id,r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
