package com.tecsup.empleados.service;

import com.tecsup.empleados.dto.EmpleadoRequest;
import com.tecsup.empleados.model.Empleado;
import com.tecsup.empleados.repository.EmpleadoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmpleadoService {
    private final EmpleadoRepository repository;

    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<Empleado> listar() {
        return repository.findAll();
    }

    public Empleado obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el empleado " + id));
    }

    public Empleado crear(EmpleadoRequest request) {
        return repository.save(new Empleado(request.nombres(), request.apellidos(),
                request.email(), request.sueldo()));
    }

    public Empleado actualizar(Long id, EmpleadoRequest request) {
        Empleado empleado = obtener(id);
        empleado.setNombres(request.nombres());
        empleado.setApellidos(request.apellidos());
        empleado.setEmail(request.email());
        empleado.setSueldo(request.sueldo());
        return repository.save(empleado);
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }
}
