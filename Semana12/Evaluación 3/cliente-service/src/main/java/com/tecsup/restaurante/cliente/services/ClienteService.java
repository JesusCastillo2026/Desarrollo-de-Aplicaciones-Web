package com.tecsup.restaurante.cliente.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tecsup.restaurante.cliente.dto.ClienteRequest;
import com.tecsup.restaurante.cliente.dto.ClienteResponse;
import com.tecsup.restaurante.cliente.model.Cliente;
import com.tecsup.restaurante.cliente.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() { return repository.findAll().stream().map(ClienteResponse::from).toList(); }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) { return ClienteResponse.from(obtener(id)); }

    public ClienteResponse crear(ClienteRequest request) {
        if (repository.existsByNumeroDocumentoIgnoreCase(request.numeroDocumento().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con ese documento.");
        }
        Cliente cliente = new Cliente();
        copiar(request, cliente);
        cliente.setActivo(request.activo() == null || request.activo());
        return ClienteResponse.from(repository.save(cliente));
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = obtener(id);
        if (repository.existsByNumeroDocumentoIgnoreCaseAndIdClienteNot(request.numeroDocumento().trim(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya pertenece a otro cliente.");
        }
        copiar(request, cliente);
        if (request.activo() != null) cliente.setActivo(request.activo());
        return ClienteResponse.from(repository.save(cliente));
    }

    public void eliminar(Long id) { repository.delete(obtener(id)); }

    private Cliente obtener(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el cliente " + id + "."));
    }

    private void copiar(ClienteRequest request, Cliente cliente) {
        cliente.setNombres(request.nombres().trim());
        cliente.setApellidos(request.apellidos().trim());
        cliente.setNumeroDocumento(request.numeroDocumento().trim());
        cliente.setCorreo(request.correo().trim().toLowerCase());
    }
}
