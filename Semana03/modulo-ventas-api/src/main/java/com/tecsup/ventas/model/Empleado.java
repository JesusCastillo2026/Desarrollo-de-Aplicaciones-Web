package com.tecsup.ventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empleados", uniqueConstraints = @UniqueConstraint(name = "uk_empleado_correo", columnNames = "correo"))
public class Empleado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80)
    private String nombres;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80)
    private String apellidos;
    @NotBlank @Email @Size(max = 120) @Column(nullable = false, length = 120)
    private String correo;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80)
    private String cargo;

    protected Empleado() {}
    public Empleado(String nombres, String apellidos, String correo, String cargo) {
        this.nombres=nombres; this.apellidos=apellidos; this.correo=correo; this.cargo=cargo;
    }
    public Long getId(){return id;}
    public String getNombres(){return nombres;} public void setNombres(String v){nombres=v;}
    public String getApellidos(){return apellidos;} public void setApellidos(String v){apellidos=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getCargo(){return cargo;} public void setCargo(String v){cargo=v;}
}
