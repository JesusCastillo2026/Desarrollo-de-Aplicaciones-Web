package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    private ProductoRepository repo;
    private ProductoService service;

    private final Map<Long, Producto> data = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @BeforeEach
    void setup() {
        data.clear();
        sequence.set(1);
        repo = (ProductoRepository) Proxy.newProxyInstance(
                ProductoRepository.class.getClassLoader(), new Class<?>[]{ProductoRepository.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findAll" -> new ArrayList<>(data.values());
                    case "findById" -> Optional.ofNullable(data.get(args[0]));
                    case "save" -> guardar((Producto) args[0]);
                    case "deleteById" -> { data.remove(args[0]); yield null; }
                    case "findByNombreContainingIgnoreCase" -> data.values().stream()
                            .filter(item -> item.getNombre().toLowerCase().contains(((String) args[0]).toLowerCase()))
                            .toList();
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        service = new ProductoService(repo);
    }

    @Test
    void listarDevuelveProductosDelRepositorio() {
        List<Producto> productos = List.of(new Producto(1L, "Monitor", 500, 10, "Pantallas"));
        productos.forEach(this::guardar);

        List<Producto> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNombre());
        assertEquals(1, data.size());
    }

    @Test
    void guardarPersisteElProducto() {
        Producto producto = new Producto(null, "Laptop", 3000, 5, "Cómputo");
        Producto resultado = service.guardar(producto);

        assertNotNull(resultado.getId());
        assertSame(producto, data.get(resultado.getId()));
    }

    @Test
    void obtenerDevuelveProductoExistente() {
        Producto producto = new Producto(1L, "Teclado", 100, 20, "Accesorios");
        data.put(1L, producto);

        Producto resultado = service.obtener(1L);

        assertSame(producto, resultado);
        assertSame(producto, data.get(1L));
    }

    @Test
    void obtenerDevuelveNullCuandoNoExiste() {
        assertNull(service.obtener(99L));
    }

    @Test
    void actualizarModificaYGuardaElProductoExistente() {
        Producto existente = new Producto(7L, "Mouse", 80, 3, "Accesorios");
        Producto cambios = new Producto(null, "Mouse inalámbrico", 95, 8, "Accesorios");
        existente.setId(7L);
        data.put(7L, existente);

        Producto resultado = service.actualizar(7L, cambios);

        assertSame(existente, resultado);
        assertEquals("Mouse inalámbrico", resultado.getNombre());
        assertEquals(95, resultado.getPrecio());
        assertEquals(8, resultado.getStock());
        assertSame(existente, data.get(7L));
    }

    @Test
    void actualizarDevuelveNullCuandoNoExiste() {
        assertNull(service.actualizar(44L, new Producto(null, "X", 10, 1, "Varios")));
        assertTrue(data.isEmpty());
    }

    @Test
    void eliminarSolicitaAlRepositorioBorrarElId() {
        service.eliminar(5L);

        assertFalse(data.containsKey(5L));
    }

    private Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(sequence.getAndIncrement());
        }
        data.put(producto.getId(), producto);
        return producto;
    }
}
