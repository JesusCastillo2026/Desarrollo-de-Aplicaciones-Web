package com.tecsup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditoriaLog {    
    
    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;    
    
    private String accion;    
    private String metodo;    
    private LocalDateTime fecha;    
    private String detalle;    

    // Constructor vacío requerido por Hibernate
    public AuditoriaLog() {}    

    // ESTE ES EL CONSTRUCTOR DE 3 PARÁMETROS QUE TE FALTA
    public AuditoriaLog(String accion, String metodo, String detalle) {        
        this.accion = accion;        
        this.metodo = metodo;        
        this.fecha = LocalDateTime.now();        
        this.detalle = detalle;    
    }    

    public Long getId() { return id; }    
    public void setId(Long id) { this.id = id; }    
    
    public String getAccion() { return accion; }    
    public void setAccion(String accion) { this.accion = accion; }    
    
    public String getMetodo() { return metodo; }    
    public void setMetodo(String metodo) { this.metodo = metodo; }    
    
    public LocalDateTime getFecha() { return fecha; }    
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }    
    
    public String getDetalle() { return detalle; }    
    public void setDetalle(String detalle) { this.detalle = detalle; }
}