package com.tecsup.cliente.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity @Table(name="cliente")
public class Cliente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String nroDni;
    private String estado;
    public Cliente() {}
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getNroDni() { return nroDni; }
    public String getEstado() { return estado; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public void setApellidos(String apellidos) { this.apellidos=apellidos; }
    public void setDireccion(String direccion) { this.direccion=direccion; }
    public void setTelefono(String telefono) { this.telefono=telefono; }
    public void setNroDni(String nroDni) { this.nroDni=nroDni; }
    public void setEstado(String estado) { this.estado=estado; }
}
