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
@Table(name = "programaciones_cirugia")
public class ProgramacionCirugia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgramacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudCirugia solicitud;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quirofano_id", nullable = false)
    private Quirofano quirofano;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cirujano_id", nullable = false)
    private Medico cirujano;

    @ManyToOne(optional = false)
    @JoinColumn(name = "anestesiologo_id", nullable = false)
    private Medico anestesiologo;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinEstimada;
    private Integer duracionEstimadaMinutos;
    private String prioridad;
    private String estado = "PROGRAMADA";
    private String motivoUltimoCambio;
    private String responsableUltimoCambio;
    private LocalDateTime fechaUltimoCambio;

    public ProgramacionCirugia() {}

    public Long getIdProgramacion() { return idProgramacion; }
    public void setIdProgramacion(Long idProgramacion) { this.idProgramacion = idProgramacion; }
    public SolicitudCirugia getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudCirugia solicitud) { this.solicitud = solicitud; }
    public Quirofano getQuirofano() { return quirofano; }
    public void setQuirofano(Quirofano quirofano) { this.quirofano = quirofano; }
    public Medico getCirujano() { return cirujano; }
    public void setCirujano(Medico cirujano) { this.cirujano = cirujano; }
    public Medico getAnestesiologo() { return anestesiologo; }
    public void setAnestesiologo(Medico anestesiologo) { this.anestesiologo = anestesiologo; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public LocalDateTime getFechaHoraFinEstimada() { return fechaHoraFinEstimada; }
    public void setFechaHoraFinEstimada(LocalDateTime fechaHoraFinEstimada) { this.fechaHoraFinEstimada = fechaHoraFinEstimada; }
    public Integer getDuracionEstimadaMinutos() { return duracionEstimadaMinutos; }
    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) { this.duracionEstimadaMinutos = duracionEstimadaMinutos; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMotivoUltimoCambio() { return motivoUltimoCambio; }
    public void setMotivoUltimoCambio(String motivoUltimoCambio) { this.motivoUltimoCambio = motivoUltimoCambio; }
    public String getResponsableUltimoCambio() { return responsableUltimoCambio; }
    public void setResponsableUltimoCambio(String responsableUltimoCambio) { this.responsableUltimoCambio = responsableUltimoCambio; }
    public LocalDateTime getFechaUltimoCambio() { return fechaUltimoCambio; }
    public void setFechaUltimoCambio(LocalDateTime fechaUltimoCambio) { this.fechaUltimoCambio = fechaUltimoCambio; }
}
