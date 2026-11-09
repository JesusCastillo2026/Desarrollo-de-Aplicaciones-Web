package com.tecsup.paciente.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity @Table(name="paciente")
public class Paciente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_paciente")
    private Long id;
    @Column(nullable=false, unique=true, length=8)
    private String dni;
    private String nombres;
    private String apellidos;
    private java.time.LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String direccion;
    private String correo;
    private String estado;
    public Paciente() {}
    public Long getId() { return id; }
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public java.time.LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getSexo() { return sexo; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getCorreo() { return correo; }
    public String getEstado() { return estado; }
    public void setDni(String dni) { this.dni=dni; }
    public void setNombres(String nombres) { this.nombres=nombres; }
    public void setApellidos(String apellidos) { this.apellidos=apellidos; }
    public void setFechaNacimiento(java.time.LocalDate fechaNacimiento) { this.fechaNacimiento=fechaNacimiento; }
    public void setSexo(String sexo) { this.sexo=sexo; }
    public void setTelefono(String telefono) { this.telefono=telefono; }
    public void setDireccion(String direccion) { this.direccion=direccion; }
    public void setCorreo(String correo) { this.correo=correo; }
    public void setEstado(String estado) { this.estado=estado; }
}
