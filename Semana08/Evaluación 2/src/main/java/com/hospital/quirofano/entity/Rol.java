package com.hospital.quirofano.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_roles_nombre", columnNames = "nombre_rol")
})
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(name = "nombre_rol", nullable = false, length = 40)
    private String nombreRol;

    @Column(nullable = false, length = 180)
    private String descripcion;

    @Column(nullable = false)
    private boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }
    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<Usuario> getUsuarios() { return usuarios; }
}
