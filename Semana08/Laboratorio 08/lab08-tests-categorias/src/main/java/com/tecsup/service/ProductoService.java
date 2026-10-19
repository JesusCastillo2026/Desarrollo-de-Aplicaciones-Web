package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    
    private final ProductoRepository repo; // Sin @Autowired

    // Inyección por constructor
    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto guardar(Producto p) {
        return repo.save(p);
    }

    public Producto actualizar(Long id, Producto cambios) {
        return repo.findById(id).map(existente -> {
            existente.setNombre(cambios.getNombre());
            existente.setPrecio(cambios.getPrecio());
            existente.setStock(cambios.getStock());
            existente.setCategoria(cambios.getCategoria());
            return repo.save(existente);
        }).orElse(null);
    }

    public Producto obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
