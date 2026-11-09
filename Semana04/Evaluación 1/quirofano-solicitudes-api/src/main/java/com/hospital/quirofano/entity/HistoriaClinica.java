package com.hospital.quirofano.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoria;
    private String numeroHistoria;

    public HistoriaClinica() {}

    public Long getIdHistoria() { return idHistoria; }
    public void setIdHistoria(Long idHistoria) { this.idHistoria = idHistoria; }
    public String getNumeroHistoria() { return numeroHistoria; }
    public void setNumeroHistoria(String numeroHistoria) { this.numeroHistoria = numeroHistoria; }
}