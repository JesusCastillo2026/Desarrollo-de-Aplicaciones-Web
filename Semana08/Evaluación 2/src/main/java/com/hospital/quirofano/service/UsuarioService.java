package com.hospital.quirofano.service;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hospital.quirofano.audit.AuditOperation;
import com.hospital.quirofano.dto.UsuarioForm;
import com.hospital.quirofano.entity.Rol;
import com.hospital.quirofano.entity.Usuario;
import com.hospital.quirofano.repository.RolRepository;
import com.hospital.quirofano.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
    }

    @Transactional
    @AuditOperation(action = "CREAR", entity = "usuarios")
    public Usuario crear(UsuarioForm form) {
        validarFormulario(form, true);
        String username = normalizarUsuario(form.getNombreUsuario());
        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está registrado.");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(form.getCorreo().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado.");
        }

        Usuario usuario = new Usuario();
        copiarDatos(usuario, form, true);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @AuditOperation(action = "MODIFICAR", entity = "usuarios")
    public Usuario actualizar(Long id, UsuarioForm form) {
        Usuario usuario = buscar(id);
        validarFormulario(form, false);
        String username = normalizarUsuario(form.getNombreUsuario());
        usuarioRepository.findByNombreUsuarioIgnoreCase(username)
                .filter(otro -> !otro.getIdUsuario().equals(id))
                .ifPresent(otro -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está registrado."); });
        usuarioRepository.findByCorreoIgnoreCase(form.getCorreo().trim())
                .filter(otro -> !otro.getIdUsuario().equals(id))
                .ifPresent(otro -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado."); });

        copiarDatos(usuario, form, false);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @AuditOperation(action = "CAMBIAR_ESTADO", entity = "usuarios")
    public Usuario cambiarEstado(Long id) {
        Usuario usuario = buscar(id);
        if (usuario.isActivo() && "ADMINISTRADOR".equals(usuario.getRol().getNombreRol())) {
            long administradoresActivos = usuarioRepository.countByRol_NombreRolAndActivoTrue("ADMINISTRADOR");
            if (administradoresActivos <= 1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Debe conservarse al menos un usuario administrador activo.");
            }
        }
        usuario.setActivo(!usuario.isActivo());
        return usuarioRepository.save(usuario);
    }

    private void copiarDatos(Usuario usuario, UsuarioForm form, boolean nuevo) {
        Rol rol = rolRepository.findById(form.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado."));
        if (!rol.isActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede asignar un rol inactivo.");
        }
        usuario.setNombreUsuario(normalizarUsuario(form.getNombreUsuario()));
        usuario.setCorreo(form.getCorreo().trim().toLowerCase(Locale.ROOT));
        usuario.setNombres(form.getNombres().trim());
        usuario.setApellidos(form.getApellidos().trim());
        usuario.setRol(rol);
        usuario.setActivo(form.isActivo());
        if (nuevo || (form.getPassword() != null && !form.getPassword().isBlank())) {
            usuario.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        }
    }

    private void validarFormulario(UsuarioForm form, boolean nuevo) {
        if (form == null || form.getRolId() == null || textoVacio(form.getNombreUsuario())
                || textoVacio(form.getCorreo()) || textoVacio(form.getNombres()) || textoVacio(form.getApellidos())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complete los datos obligatorios del usuario.");
        }
        if (nuevo && textoVacio(form.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria para crear el usuario.");
        }
        if (form.getPassword() != null && !form.getPassword().isBlank() && form.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 8 caracteres.");
        }
    }

    private String normalizarUsuario(String valor) {
        return valor.trim().toLowerCase(Locale.ROOT);
    }

    private boolean textoVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
