package com.tecsup.controller;

import java.util.List;

public class Persona {
    private String nombre;
    private String apellidos;
    private String estadoCivil;
    private String carrera;
    private String instituto;
    private String distrito;
    private String equipo;
    private List<String> pasatiempos;

    public Persona(String nombre, String apellidos, String estadoCivil, String carrera, String instituto,
                   String distrito, String equipo, List<String> pasatiempos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.estadoCivil = estadoCivil;
        this.carrera = carrera;
        this.instituto = instituto;
        this.distrito = distrito;
        this.equipo = equipo;
        this.pasatiempos = List.copyOf(pasatiempos);
    }

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getEstadoCivil() { return estadoCivil; }
    public String getCarrera() { return carrera; }
    public String getInstituto() { return instituto; }
    public String getDistrito() { return distrito; }
    public String getEquipo() { return equipo; }
    public List<String> getPasatiempos() { return pasatiempos; }
}
