package com.hospital.quirofano.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "aprobaciones_cirugia")
public class AprobacionCirugia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAprobacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudCirugia solicitud;

    private String estado;
    private String responsable;
    private LocalDateTime fechaDecision;
    private String observacion;
    private String motivo;

    public AprobacionCirugia() {}

    public Long getIdAprobacion() { return idAprobacion; }
    public void setIdAprobacion(Long idAprobacion) { this.idAprobacion = idAprobacion; }
    public SolicitudCirugia getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudCirugia solicitud) { this.solicitud = solicitud; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public LocalDateTime getFechaDecision() { return fechaDecision; }
    public void setFechaDecision(LocalDateTime fechaDecision) { this.fechaDecision = fechaDecision; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
