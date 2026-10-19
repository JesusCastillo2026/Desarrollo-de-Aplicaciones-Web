package com.hospital.quirofano.dto;

import com.hospital.quirofano.entity.Usuario;

public record UsuarioResponse(
        Long idUsuario,
        String nombreUsuario,
        String correo,
        String nombres,
        String apellidos,
        Long rolId,
        String nombreRol,
        boolean activo) {

    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getNombreUsuario(), usuario.getCorreo(),
                usuario.getNombres(), usuario.getApellidos(), usuario.getRol().getIdRol(),
                usuario.getRol().getNombreRol(), usuario.isActivo());
    }
}
