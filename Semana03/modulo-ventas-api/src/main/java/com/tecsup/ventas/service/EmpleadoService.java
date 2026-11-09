package com.tecsup.ventas.service;

import com.tecsup.ventas.dto.EmpleadoRequest;
import com.tecsup.ventas.model.Empleado;
import com.tecsup.ventas.repository.EmpleadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repository;
    public EmpleadoService(EmpleadoRepository repository) { this.repository=repository; }
    public List<Empleado> listar() { return repository.findAll(); }
    public Empleado obtener(Long id) { return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Empleado " + id + " no encontrado")); }
    public Empleado crear(EmpleadoRequest r) { return repository.save(new Empleado(r.nombres(), r.apellidos(), r.correo(), r.cargo())); }
    @Transactional public Empleado actualizar(Long id, EmpleadoRequest r) { Empleado e=obtener(id); e.setNombres(r.nombres()); e.setApellidos(r.apellidos()); e.setCorreo(r.correo()); e.setCargo(r.cargo()); return e; }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
