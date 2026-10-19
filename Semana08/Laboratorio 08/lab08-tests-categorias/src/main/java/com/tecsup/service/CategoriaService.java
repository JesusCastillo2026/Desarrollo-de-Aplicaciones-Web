package com.tecsup.service;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtener(Long id) {
        return toDto(encontrar(id));
    }

    public CategoriaDTO crear(CategoriaDTO dto) {
        String nombre = dto.getNombre().trim();
        if (repository.existsByNombreIgnoreCase(nombre)) {
            throw new DuplicateResourceException("Ya existe una categoría con ese nombre.");
        }
        Categoria nueva = new Categoria(nombre, limpiar(dto.getDescripcion()));
        return toDto(repository.save(nueva));
    }

    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria existente = encontrar(id);
        String nombre = dto.getNombre().trim();
        if (repository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new DuplicateResourceException("Ya existe otra categoría con ese nombre.");
        }
        existente.setNombre(nombre);
        existente.setDescripcion(limpiar(dto.getDescripcion()));
        return toDto(repository.save(existente));
    }

    public void eliminar(Long id) {
        Categoria existente = encontrar(id);
        repository.delete(existente);
    }

    private Categoria encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la categoría con ID " + id + "."));
    }

    private CategoriaDTO toDto(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }

    private String limpiar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
