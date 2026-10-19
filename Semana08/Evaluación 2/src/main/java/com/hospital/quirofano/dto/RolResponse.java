package com.hospital.quirofano.dto;

import com.hospital.quirofano.entity.Rol;

public record RolResponse(Long id, String nombre, String descripcion, boolean activo) {
    public static RolResponse desde(Rol rol) {
        return new RolResponse(rol.getIdRol(), rol.getNombreRol(), rol.getDescripcion(), rol.isActivo());
    }
}
