package com.tecsup.service;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
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

class CategoriaServiceTest {

    private CategoriaRepository repository;
    private CategoriaService service;

    private final Map<Long, Categoria> data = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @BeforeEach
    void setup() {
        data.clear();
        sequence.set(1);
        repository = (CategoriaRepository) Proxy.newProxyInstance(
                CategoriaRepository.class.getClassLoader(), new Class<?>[]{CategoriaRepository.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "findAll" -> new ArrayList<>(data.values());
                    case "findById" -> Optional.ofNullable(data.get(args[0]));
                    case "existsByNombreIgnoreCase" -> data.values().stream()
                            .anyMatch(item -> item.getNombre().equalsIgnoreCase((String) args[0]));
                    case "existsByNombreIgnoreCaseAndIdNot" -> data.values().stream()
                            .anyMatch(item -> !item.getId().equals(args[1])
                                    && item.getNombre().equalsIgnoreCase((String) args[0]));
                    case "save" -> guardar((Categoria) args[0]);
                    case "delete" -> { data.remove(((Categoria) args[0]).getId()); yield null; }
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        service = new CategoriaService(repository);
    }

    @Test
    void listarConvierteLasEntidadesADto() {
        guardar(new Categoria("Accesorios", "Periféricos"));

        List<CategoriaDTO> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Accesorios", resultado.get(0).getNombre());
    }

    @Test
    void crearRecortaElNombreYGuardaLaCategoria() {
        CategoriaDTO creada = service.crear(new CategoriaDTO(null, " Cómputo ", " Equipos "));

        assertEquals("Cómputo", creada.getNombre());
        assertEquals("Equipos", creada.getDescripcion());
        assertEquals(1, data.size());
    }

    @Test
    void crearRechazaUnNombreDuplicado() {
        guardar(new Categoria("Accesorios", null));

        assertThrows(DuplicateResourceException.class,
                () -> service.crear(new CategoriaDTO(null, "Accesorios", null)));
        assertEquals(1, data.size());
    }

    @Test
    void obtenerDevuelveCategoriaExistente() {
        Categoria categoria = new Categoria("Cómputo", null);
        categoria.setId(1L);
        data.put(1L, categoria);

        assertEquals("Cómputo", service.obtener(1L).getNombre());
    }

    @Test
    void obtenerLanza404DeDominioSiNoExiste() {
        assertThrows(ResourceNotFoundException.class, () -> service.obtener(99L));
    }

    @Test
    void actualizarGuardaLosNuevosDatos() {
        Categoria existente = new Categoria("Periféricos", "Entrada y salida");
        existente.setId(4L);
        data.put(4L, existente);

        CategoriaDTO actualizada = service.actualizar(4L, new CategoriaDTO(null, "Accesorios", "Dispositivos"));

        assertEquals("Accesorios", actualizada.getNombre());
        assertEquals("Dispositivos", actualizada.getDescripcion());
        assertSame(existente, data.get(4L));
    }

    @Test
    void eliminarBorraLaCategoriaEncontrada() {
        Categoria categoria = new Categoria("Varios", null);
        categoria.setId(3L);
        data.put(3L, categoria);

        service.eliminar(3L);

        assertFalse(data.containsKey(3L));
    }

    private Categoria guardar(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setId(sequence.getAndIncrement());
        }
        data.put(categoria.getId(), categoria);
        return categoria;
    }
}
