package com.hospital.quirofano.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.quirofano.entity.SolicitudCirugia;
import com.hospital.quirofano.service.SolicitudCirugiaService;

@Controller
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    private final SolicitudCirugiaService solicitudService;

    public SolicitudWebController(SolicitudCirugiaService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String listarSolicitudes(Model model) {
        model.addAttribute("solicitudes", solicitudService.listarTodas());
        return "lista-solicitudes";
    }

    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
}

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("solicitud", new SolicitudCirugia());
        model.addAttribute("pacientes", solicitudService.listarPacientes());
        model.addAttribute("medicos", solicitudService.listarMedicos());
        model.addAttribute("historias", solicitudService.listarHistorias());
        return "formulario-solicitud";
    }

    @PostMapping("/guardar")
    public String guardarSolicitud(@ModelAttribute SolicitudCirugia solicitud) {    
        solicitudService.registrarSolicitud(solicitud);
        return "redirect:/web/solicitudes";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarSolicitudWeb(@PathVariable Long id, @RequestParam(defaultValue = "Cancelado por el usuario desde la web") String motivo) {
        solicitudService.cancelarSolicitud(id, motivo);
        return "redirect:/web/solicitudes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarSolicitudWeb(@PathVariable Long id) {
        solicitudService.eliminarSolicitud(id);
        return "redirect:/web/solicitudes";
    }
}