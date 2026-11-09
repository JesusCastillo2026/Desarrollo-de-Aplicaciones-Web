package com.tecsup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PerfilRestController {

    @GetMapping("/perfil")
    public Persona obtenerPerfilJson() {
        return new Persona(
                "Jesus",
                "Castillo Sumire",
                "Soltero",
                "Diseño y Desarrollo de Software",
                "Tecsup",
                "Santa Anita",
                "Universitario de Deportes",
                java.util.List.of("Fútbol", "Videojuegos", "Ver series")
        );
    }
}
