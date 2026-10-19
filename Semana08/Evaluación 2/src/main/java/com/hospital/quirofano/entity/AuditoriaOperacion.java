package com.hospital.quirofano.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "auditoria_operaciones", indexes = {
        @Index(name = "idx_auditoria_fecha", columnList = "fecha_hora"),
        @Index(name = "idx_auditoria_entidad_registro", columnList = "entidad, registro_id")
})
public class AuditoriaOperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAuditoria;

    @Column(nullable = false, length = 80)
    private String usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 40)
    private String operacion;

    @Column(nullable = false, length = 80)
    private String entidad;

    @Column(name = "registro_id", length = 80)
    private String registroId;

    @Column(length = 180)
    private String detalle;

    public Long getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Long idAuditoria) { this.idAuditoria = idAuditoria; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    public String getRegistroId() { return registroId; }
    public void setRegistroId(String registroId) { this.registroId = registroId; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}
