package com.tecsup.producto.service;

import com.tecsup.producto.client.CategoriaClient;
import com.tecsup.producto.dto.CategoriaResponse;
import com.tecsup.producto.dto.ProductoRequest;
import com.tecsup.producto.model.Producto;
import com.tecsup.producto.repository.ProductoRepository;
import feign.FeignException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    private final CategoriaClient categoriaClient;

    public ProductoService(ProductoRepository repo, CategoriaClient categoriaClient) {
        this.repo = repo;
        this.categoriaClient = categoriaClient;
    }

    public List<Producto> listar() {
        return repo.findAll().stream().map(this::completarCategoria).toList();
    }

    public Producto obtener(Long id) {
        return repo.findById(id)
                .map(this::completarCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public Producto crear(ProductoRequest request) {
        Producto producto = new Producto();
        aplicar(producto, request);
        return completarCategoria(repo.save(producto));
    }

    public Producto actualizar(Long id, ProductoRequest request) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        aplicar(producto, request);
        return completarCategoria(repo.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        repo.delete(producto);
    }

    private void aplicar(Producto producto, ProductoRequest request) {
        obtenerCategoria(request.categoriaId());
        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCategoriaId(request.categoriaId());
    }

    private Producto completarCategoria(Producto producto) {
        producto.setCategoria(obtenerCategoria(producto.getCategoriaId()));
        return producto;
    }

    private CategoriaResponse obtenerCategoria(Long id) {
        try {
            return categoriaClient.obtenerCategoria(id);
        } catch (FeignException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría " + id + " no existe");
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "El servicio de categorías no está disponible");
        }
    }
}
