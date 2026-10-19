package com.hospital.quirofano.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_cirugia")
public class SolicitudCirugia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    private String numeroSolicitud;
    private String procedimiento;
    private String diagnostico;
    private String motivo;
    private String prioridad;
    private String observaciones;
    private String estado;
    private String fecha;
    private String origen;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_solicitante_id")
    private Medico medicoSolicitante;

    @ManyToOne
    @JoinColumn(name = "historia_clinica_id")
    private HistoriaClinica historiaClinica;

    public SolicitudCirugia() {
    }

    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }

    public String getNumeroSolicitud() { return numeroSolicitud; }
    public void setNumeroSolicitud(String numeroSolicitud) { this.numeroSolicitud = numeroSolicitud; }

    public String getProcedimiento() { return procedimiento; }
    public void setProcedimiento(String procedimiento) { this.procedimiento = procedimiento; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedicoSolicitante() { return medicoSolicitante; }
    public void setMedicoSolicitante(Medico medicoSolicitante) { this.medicoSolicitante = medicoSolicitante; }

    public HistoriaClinica getHistoriaClinica() { return historiaClinica; }
    public void setHistoriaClinica(HistoriaClinica historiaClinica) { this.historiaClinica = historiaClinica; }
}