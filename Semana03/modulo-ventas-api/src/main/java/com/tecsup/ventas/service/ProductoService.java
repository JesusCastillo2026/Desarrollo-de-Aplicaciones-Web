package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.ProductoRequest;
import com.tecsup.ventas.model.Categoria;
import com.tecsup.ventas.model.Producto;
import com.tecsup.ventas.repository.CategoriaRepository;
import com.tecsup.ventas.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;
    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) { this.repository=repository; this.categoriaRepository=categoriaRepository; }
    public List<Producto> listar() { return repository.findAll(); }
    public Producto obtener(Long id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Producto " + id + " no encontrado")); }
    public Producto crear(ProductoRequest r) { return repository.save(new Producto(r.nombre(), r.precio(), r.stock(), categoria(r.categoriaId()))); }
    @Transactional public Producto actualizar(Long id, ProductoRequest r) { Producto p=obtener(id); p.setNombre(r.nombre()); p.setPrecio(r.precio()); p.setStock(r.stock()); p.setCategoria(categoria(r.categoriaId())); return p; }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
    private Categoria categoria(Long id) { return categoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoría " + id + " no encontrada")); }
}
