package com.tecsup.medico.controller;

import com.tecsup.medico.model.Especialidad;
import com.tecsup.medico.repository.EspecialidadRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {
    private final EspecialidadRepository repository;

    public EspecialidadController(EspecialidadRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Especialidad> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Especialidad obtener(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Especialidad no encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidad crear(@Valid @RequestBody Especialidad especialidad) {
        return repository.save(especialidad);
    }

    @PutMapping("/{id}")
    public Especialidad actualizar(@PathVariable Long id, @Valid @RequestBody Especialidad datos) {
        Especialidad actual = obtener(id);
        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());
        actual.setEstado(datos.getEstado());
        return repository.save(actual);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repository.delete(obtener(id));
    }
}
