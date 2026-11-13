package com.tecsup.categoria.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity @Table(name="categoria")
public class Categoria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String estado;
    public Categoria() {}
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public void setEstado(String estado) { this.estado=estado; }
}
