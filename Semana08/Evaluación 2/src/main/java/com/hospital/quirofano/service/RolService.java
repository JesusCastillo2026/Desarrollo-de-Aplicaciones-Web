package com.hospital.quirofano.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hospital.quirofano.audit.AuditOperation;
import com.hospital.quirofano.dto.RolForm;
import com.hospital.quirofano.entity.Rol;
import com.hospital.quirofano.repository.RolRepository;
import com.hospital.quirofano.repository.UsuarioRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> listarTodos() { return rolRepository.findAll(); }

    @Transactional(readOnly = true)
    public List<Rol> listarActivos() {
        return rolRepository.findAll().stream().filter(Rol::isActivo).toList();
    }

    @Transactional(readOnly = true)
    public Rol buscar(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado."));
    }

    @Transactional
    @AuditOperation(action = "CREAR", entity = "roles")
    public Rol crear(RolForm form) {
        validar(form);
        String nombre = normalizarNombre(form.getNombreRol());
        if (rolRepository.existsByNombreRolIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El rol ya está registrado.");
        }
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        rol.setDescripcion(form.getDescripcion().trim());
        rol.setActivo(form.isActivo());
        return rolRepository.save(rol);
    }

    @Transactional
    @AuditOperation(action = "MODIFICAR", entity = "roles")
    public Rol actualizar(Long id, RolForm form) {
        validar(form);
        Rol rol = buscar(id);
        String nombre = normalizarNombre(form.getNombreRol());
        rolRepository.findByNombreRolIgnoreCase(nombre)
                .filter(otro -> !otro.getIdRol().equals(id))
                .ifPresent(otro -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "El rol ya está registrado."); });
        rol.setNombreRol(nombre);
        rol.setDescripcion(form.getDescripcion().trim());
        return rolRepository.save(rol);
    }

    @Transactional
    @AuditOperation(action = "CAMBIAR_ESTADO", entity = "roles")
    public Rol cambiarEstado(Long id) {
        Rol rol = buscar(id);
        if (rol.isActivo() && usuarioRepository.existsByRol_IdRolAndActivoTrue(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede desactivar un rol asignado a usuarios activos.");
        }
        rol.setActivo(!rol.isActivo());
        return rolRepository.save(rol);
    }

    private void validar(RolForm form) {
        if (form == null || textoVacio(form.getNombreRol()) || textoVacio(form.getDescripcion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingrese el nombre y la descripción del rol.");
        }
    }

    private String normalizarNombre(String valor) {
        String ascii = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return ascii.toUpperCase(Locale.ROOT).replaceAll("\\s+", "_");
    }

    private boolean textoVacio(String valor) { return valor == null || valor.isBlank(); }
}
