package com.tecsup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/mi-perfil";
    }

    @GetMapping("/mi-perfil")
    public String mostrarPerfilWeb(Model model) {
        Persona jesus = new Persona(
                "Jesus",
                "Castillo Sumire",
                "Soltero",
                "Diseño y Desarrollo de Software",
                "Tecsup",
                "Santa Anita",
                "Universitario de Deportes",
                java.util.List.of("Fútbol", "Videojuegos", "Ver series")
        );

        model.addAttribute("persona", jesus);
        return "perfil"; // Esto llama a perfil.html
    }
}
