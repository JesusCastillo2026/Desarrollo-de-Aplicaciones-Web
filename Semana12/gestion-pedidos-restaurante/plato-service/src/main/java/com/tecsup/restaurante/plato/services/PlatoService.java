package com.tecsup.restaurante.plato.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tecsup.restaurante.plato.dto.PlatoRequest;
import com.tecsup.restaurante.plato.dto.PlatoResponse;
import com.tecsup.restaurante.plato.model.Plato;
import com.tecsup.restaurante.plato.repository.PlatoRepository;

@Service
@Transactional
public class PlatoService {
    private final PlatoRepository repository;

    public PlatoService(PlatoRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<PlatoResponse> listar() { return repository.findAll().stream().map(PlatoResponse::from).toList(); }

    @Transactional(readOnly = true)
    public PlatoResponse buscar(Long id) { return PlatoResponse.from(obtener(id)); }

    public PlatoResponse crear(PlatoRequest request) {
        if (repository.existsByCodigoIgnoreCase(request.codigo().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un plato con ese código.");
        }
        Plato plato = new Plato();
        copiar(request, plato);
        plato.setDisponible(request.disponible() == null || request.disponible());
        return PlatoResponse.from(repository.save(plato));
    }

    public PlatoResponse actualizar(Long id, PlatoRequest request) {
        Plato plato = obtener(id);
        if (repository.existsByCodigoIgnoreCaseAndIdPlatoNot(request.codigo().trim(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El código ya pertenece a otro plato.");
        }
        copiar(request, plato);
        if (request.disponible() != null) plato.setDisponible(request.disponible());
        return PlatoResponse.from(repository.save(plato));
    }

    public void eliminar(Long id) { repository.delete(obtener(id)); }

    private Plato obtener(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el plato " + id + "."));
    }

    private void copiar(PlatoRequest request, Plato plato) {
        plato.setCodigo(request.codigo().trim().toUpperCase());
        plato.setNombre(request.nombre().trim());
        plato.setDescripcion(request.descripcion() == null ? null : request.descripcion().trim());
        plato.setPrecio(request.precio());
    }
}
