package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.CategoriaRequest;
import com.tecsup.ventas.model.Categoria;
import com.tecsup.ventas.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;
    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }
    public List<Categoria> listar() { return repository.findAll(); }
    public Categoria obtener(Long id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoría " + id + " no encontrada")); }
    public Categoria crear(CategoriaRequest r) { return repository.save(new Categoria(r.nombre(), r.descripcion())); }
    @Transactional public Categoria actualizar(Long id, CategoriaRequest r) { Categoria e=obtener(id); e.setNombre(r.nombre()); e.setDescripcion(r.descripcion()); return e; }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
