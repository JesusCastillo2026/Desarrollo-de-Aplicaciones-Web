package com.hospital.quirofano.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quirofanos")
public class Quirofano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuirofano;

    private String codigo;
    private String nombre;
    private String area;
    private Integer capacidad;
    private String equipamiento;
    private String estado = "DISPONIBLE";

    public Quirofano() {}

    public Long getIdQuirofano() { return idQuirofano; }
    public void setIdQuirofano(Long idQuirofano) { this.idQuirofano = idQuirofano; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getEquipamiento() { return equipamiento; }
    public void setEquipamiento(String equipamiento) { this.equipamiento = equipamiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
