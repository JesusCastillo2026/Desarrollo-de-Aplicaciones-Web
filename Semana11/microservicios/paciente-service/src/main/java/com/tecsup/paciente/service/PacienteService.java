package com.tecsup.paciente.service;
import com.tecsup.paciente.dto.PacienteRequest;
import com.tecsup.paciente.model.Paciente;
import com.tecsup.paciente.repository.PacienteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class PacienteService {
  private final PacienteRepository repository;
  public PacienteService(PacienteRepository repository) { this.repository=repository; }
  public List<Paciente> listar() { return repository.findAll(); }
  public Paciente obtener(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No existe el recurso " + id)); }
  public Paciente crear(PacienteRequest dto) { Paciente e=new Paciente(); aplicar(e,dto); return repository.save(e); }
  public Paciente actualizar(Long id,PacienteRequest dto) { Paciente e=obtener(id); aplicar(e,dto); return repository.save(e); }
  private void aplicar(Paciente e,PacienteRequest d) {
    e.setDni(d.dni());
    e.setNombres(d.nombres());
    e.setApellidos(d.apellidos());
    e.setFechaNacimiento(d.fechaNacimiento());
    e.setSexo(d.sexo());
    e.setTelefono(d.telefono());
    e.setDireccion(d.direccion());
    e.setCorreo(d.correo());
    e.setEstado(d.estado());
  }
  public void eliminar(Long id) { repository.delete(obtener(id)); }
}
