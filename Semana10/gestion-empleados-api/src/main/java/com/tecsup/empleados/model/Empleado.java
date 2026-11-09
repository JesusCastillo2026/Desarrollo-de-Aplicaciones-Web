package com.tecsup.empleados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String nombres;
    @Column(length = 100, nullable = false)
    private String apellidos;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal sueldo;

    protected Empleado() {}

    public Empleado(String nombres, String apellidos, String email, BigDecimal sueldo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.sueldo = sueldo;
    }

    public Long getId() { return id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getSueldo() { return sueldo; }
    public void setSueldo(BigDecimal sueldo) { this.sueldo = sueldo; }
}
