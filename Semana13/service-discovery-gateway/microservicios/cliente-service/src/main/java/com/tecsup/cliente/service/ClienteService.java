package com.tecsup.cliente.service;
import com.tecsup.cliente.dto.ClienteRequest;
import com.tecsup.cliente.model.Cliente;
import com.tecsup.cliente.repository.ClienteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class ClienteService {
  private final ClienteRepository repository;
  public ClienteService(ClienteRepository repository) { this.repository=repository; }
  public List<Cliente> listar() { return repository.findAll(); }
  public Cliente obtener(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No existe el recurso " + id)); }
  public Cliente crear(ClienteRequest dto) { Cliente e=new Cliente(); aplicar(e,dto); return repository.save(e); }
  public Cliente actualizar(Long id,ClienteRequest dto) { Cliente e=obtener(id); aplicar(e,dto); return repository.save(e); }
  private void aplicar(Cliente e,ClienteRequest d) {
    e.setNombre(d.nombre());
    e.setApellidos(d.apellidos());
    e.setDireccion(d.direccion());
    e.setTelefono(d.telefono());
    e.setNroDni(d.nroDni());
    e.setEstado(d.estado());
  }
  public void eliminar(Long id) { repository.delete(obtener(id)); }
}
