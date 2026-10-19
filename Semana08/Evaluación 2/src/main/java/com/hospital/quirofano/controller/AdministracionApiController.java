package com.hospital.quirofano.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.hospital.quirofano.dto.*;
import com.hospital.quirofano.entity.AuditoriaOperacion;
import com.hospital.quirofano.service.AuditoriaService;
import com.hospital.quirofano.service.RolService;
import com.hospital.quirofano.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AdministracionApiController {
    private final UsuarioService usuarios;
    private final RolService roles;
    private final AuditoriaService auditoria;

    public AdministracionApiController(UsuarioService usuarios, RolService roles, AuditoriaService auditoria) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.auditoria = auditoria;
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponse> usuarios() { return usuarios.listarTodos().stream().map(UsuarioResponse::desde).toList(); }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crearUsuario(@Valid @RequestBody UsuarioForm form) { return UsuarioResponse.desde(usuarios.crear(form)); }

    @PutMapping("/usuarios/{id}")
    public UsuarioResponse editarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioForm form) {
        return UsuarioResponse.desde(usuarios.actualizar(id, form));
    }

    @PutMapping("/usuarios/{id}/estado")
    public UsuarioResponse cambiarEstadoUsuario(@PathVariable Long id) { return UsuarioResponse.desde(usuarios.cambiarEstado(id)); }

    @GetMapping("/roles")
    public List<RolResponse> roles() { return roles.listarTodos().stream().map(RolResponse::desde).toList(); }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RolResponse crearRol(@Valid @RequestBody RolForm form) { return RolResponse.desde(roles.crear(form)); }

    @PutMapping("/roles/{id}")
    public RolResponse editarRol(@PathVariable Long id, @Valid @RequestBody RolForm form) {
        return RolResponse.desde(roles.actualizar(id, form));
    }

    @PutMapping("/roles/{id}/estado")
    public RolResponse cambiarEstadoRol(@PathVariable Long id) { return RolResponse.desde(roles.cambiarEstado(id)); }

    @GetMapping("/auditoria")
    public List<AuditoriaOperacion> auditoria() { return auditoria.listarRecientes(); }
}
