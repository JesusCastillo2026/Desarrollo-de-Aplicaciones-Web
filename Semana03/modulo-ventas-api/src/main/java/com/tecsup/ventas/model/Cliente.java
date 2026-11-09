package com.tecsup.ventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_correo", columnNames = "correo"),
        @UniqueConstraint(name = "uk_cliente_documento", columnNames = "documento")})
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80)
    private String nombres;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80)
    private String apellidos;
    @NotBlank @Email @Size(max = 120) @Column(nullable = false, length = 120)
    private String correo;
    @NotBlank @Size(max = 20) @Column(nullable = false, length = 20)
    private String documento;
    @Size(max = 30) private String telefono;

    protected Cliente() {}
    public Cliente(String nombres, String apellidos, String correo, String documento, String telefono) {
        this.nombres=nombres; this.apellidos=apellidos; this.correo=correo; this.documento=documento; this.telefono=telefono;
    }
    public Long getId(){return id;}
    public String getNombres(){return nombres;} public void setNombres(String v){nombres=v;}
    public String getApellidos(){return apellidos;} public void setApellidos(String v){apellidos=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getDocumento(){return documento;} public void setDocumento(String v){documento=v;}
    public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
}
