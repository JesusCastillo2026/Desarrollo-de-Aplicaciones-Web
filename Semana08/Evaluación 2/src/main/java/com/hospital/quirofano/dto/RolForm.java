package com.hospital.quirofano.dto;

import jakarta.validation.constraints.NotBlank;

public class RolForm {
    private Long idRol;

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombreRol;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private boolean activo = true;

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }
    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
