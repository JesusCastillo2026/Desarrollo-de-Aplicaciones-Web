package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.ClienteRequest;
import com.tecsup.ventas.model.Cliente;
import com.tecsup.ventas.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository) { this.repository=repository; }
    public List<Cliente> listar() { return repository.findAll(); }
    public Cliente obtener(Long id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente " + id + " no encontrado")); }
    public Cliente crear(ClienteRequest r) { return repository.save(new Cliente(r.nombres(), r.apellidos(), r.correo(), r.documento(), r.telefono())); }
    @Transactional public Cliente actualizar(Long id, ClienteRequest r) { Cliente e=obtener(id); e.setNombres(r.nombres()); e.setApellidos(r.apellidos()); e.setCorreo(r.correo()); e.setDocumento(r.documento()); e.setTelefono(r.telefono()); return e; }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
