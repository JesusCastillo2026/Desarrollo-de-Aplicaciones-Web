package com.hospital.quirofano.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "integrantes_equipo_quirurgico")
public class IntegranteEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIntegrante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "programacion_id", nullable = false)
    private ProgramacionCirugia programacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    private String funcion;

    public IntegranteEquipo() {}

    public Long getIdIntegrante() { return idIntegrante; }
    public void setIdIntegrante(Long idIntegrante) { this.idIntegrante = idIntegrante; }
    public ProgramacionCirugia getProgramacion() { return programacion; }
    public void setProgramacion(ProgramacionCirugia programacion) { this.programacion = programacion; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public String getFuncion() { return funcion; }
    public void setFuncion(String funcion) { this.funcion = funcion; }
}
