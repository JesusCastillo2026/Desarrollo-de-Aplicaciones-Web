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
@Table(name = "evaluaciones_preoperatorias")
public class EvaluacionPreoperatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvaluacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudCirugia solicitud;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Medico profesional;

    private LocalDateTime fechaEvaluacion;
    private String antecedentesRelevantes;
    private String alergias;
    private String medicacionActual;
    private String observaciones;
    private String estado;

    public EvaluacionPreoperatoria() {}

    public Long getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(Long idEvaluacion) { this.idEvaluacion = idEvaluacion; }
    public SolicitudCirugia getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudCirugia solicitud) { this.solicitud = solicitud; }
    public Medico getProfesional() { return profesional; }
    public void setProfesional(Medico profesional) { this.profesional = profesional; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }
    public String getAntecedentesRelevantes() { return antecedentesRelevantes; }
    public void setAntecedentesRelevantes(String antecedentesRelevantes) { this.antecedentesRelevantes = antecedentesRelevantes; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getMedicacionActual() { return medicacionActual; }
    public void setMedicacionActual(String medicacionActual) { this.medicacionActual = medicacionActual; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
