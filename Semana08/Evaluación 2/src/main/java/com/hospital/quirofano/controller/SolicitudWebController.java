package com.hospital.quirofano.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import com.hospital.quirofano.dto.AprobacionRequest;
import com.hospital.quirofano.dto.EvaluacionRequest;
import com.hospital.quirofano.dto.IntegranteRequest;
import com.hospital.quirofano.dto.ProgramacionRequest;
import com.hospital.quirofano.entity.ProgramacionCirugia;
import com.hospital.quirofano.entity.Quirofano;
import com.hospital.quirofano.entity.SolicitudCirugia;
import com.hospital.quirofano.service.GestionQuirurgicaService;
import com.hospital.quirofano.service.SolicitudCirugiaService;

@Controller
@RequestMapping("/web/solicitudes")
public class SolicitudWebController {

    private final SolicitudCirugiaService solicitudService;
    private final GestionQuirurgicaService gestionService;

    public SolicitudWebController(SolicitudCirugiaService solicitudService, GestionQuirurgicaService gestionService) {
        this.solicitudService = solicitudService;
        this.gestionService = gestionService;
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

    @GetMapping("/{id}/evaluacion")
    public String mostrarEvaluacion(@PathVariable Long id, Model model) {
        SolicitudCirugia solicitud = solicitudService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la solicitud " + id));
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("evaluaciones", gestionService.listarEvaluaciones(id, null));
        model.addAttribute("aprobaciones", gestionService.listarDecisiones(id));
        model.addAttribute("medicos", solicitudService.listarMedicos());
        return "evaluacion-preoperatoria";
    }

    @PostMapping("/{id}/evaluacion")
    public String guardarEvaluacion(@PathVariable Long id,
            @RequestParam Long profesionalId,
            @RequestParam String estado,
            @RequestParam(required = false) String antecedentesRelevantes,
            @RequestParam(required = false) String alergias,
            @RequestParam(required = false) String medicacionActual,
            @RequestParam(required = false) String observaciones) {
        gestionService.registrarEvaluacion(new EvaluacionRequest(id, profesionalId, antecedentesRelevantes,
                alergias, medicacionActual, observaciones, estado));
        return "redirect:/web/solicitudes/" + id + "/evaluacion";
    }

    @PostMapping("/{id}/decision")
    public String guardarDecision(@PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) String responsable,
            @RequestParam(required = false) String observacion,
            @RequestParam(required = false) String motivo,
            Principal principal) {
        gestionService.registrarDecision(id, new AprobacionRequest(estado, responsableActual(responsable, principal), observacion, motivo));
        return "redirect:/web/solicitudes/" + id + "/evaluacion";
    }

    @GetMapping("/{id}/programar")
    public String mostrarFormularioProgramacion(@PathVariable Long id, Model model) {
        SolicitudCirugia solicitud = solicitudService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la solicitud " + id));
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("quirofanos", gestionService.listarQuirofanos());
        model.addAttribute("medicos", solicitudService.listarMedicos());
        model.addAttribute("programacion", null);
        model.addAttribute("equipo", List.of());
        return "formulario-programacion";
    }

    @GetMapping("/programaciones")
    public String listarProgramaciones(Model model) {
        model.addAttribute("programaciones", gestionService.listarProgramaciones(null));
        return "lista-programaciones";
    }

    @GetMapping("/programaciones/{id}/editar")
    public String editarProgramacion(@PathVariable Long id, Model model) {
        ProgramacionCirugia programacion = gestionService.buscarProgramacion(id);
        model.addAttribute("programacion", programacion);
        model.addAttribute("solicitud", programacion.getSolicitud());
        model.addAttribute("quirofanos", gestionService.listarQuirofanos());
        model.addAttribute("medicos", solicitudService.listarMedicos());
        model.addAttribute("equipo", gestionService.listarEquipo(id));
        return "formulario-programacion";
    }

    @PostMapping("/programaciones/guardar")
    public String guardarProgramacion(@RequestParam(required = false) Long idProgramacion,
            @RequestParam Long solicitudId,
            @RequestParam Long quirofanoId,
            @RequestParam Long cirujanoId,
            @RequestParam Long anestesiologoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHoraInicio,
            @RequestParam Integer duracionEstimadaMinutos,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false) String motivoCambio,
            @RequestParam(required = false) String responsableCambio,
            @RequestParam(required = false) List<Long> integrantesMedicoId,
            @RequestParam(required = false) List<String> integrantesFunciones,
            Principal principal) {
        List<IntegranteRequest> equipo = new java.util.ArrayList<>();
        if (integrantesMedicoId != null) {
            for (int i = 0; i < integrantesMedicoId.size(); i++) {
                Long medicoId = integrantesMedicoId.get(i);
                String funcion = integrantesFunciones != null && i < integrantesFunciones.size() ? integrantesFunciones.get(i) : null;
                if (medicoId != null && funcion != null && !funcion.isBlank()) {
                    equipo.add(new IntegranteRequest(medicoId, funcion));
                }
            }
        }
        ProgramacionRequest datos = new ProgramacionRequest(solicitudId, quirofanoId, cirujanoId, anestesiologoId,
                fechaHoraInicio, duracionEstimadaMinutos, prioridad, motivoCambio, responsableActual(responsableCambio, principal), equipo);
        if (idProgramacion == null) gestionService.programar(datos);
        else gestionService.reprogramar(idProgramacion, datos);
        return "redirect:/web/programaciones";
    }

    @PostMapping("/programaciones/{id}/cancelar")
    public String cancelarProgramacion(@PathVariable Long id,
            @RequestParam(required = false) String responsable,
            @RequestParam String motivo,
            Principal principal) {
        gestionService.cancelarProgramacion(id, responsableActual(responsable, principal), motivo);
        return "redirect:/web/programaciones";
    }

    @GetMapping("/quirofanos")
    public String gestionarQuirofanos(Model model) {
        model.addAttribute("quirofanos", gestionService.listarQuirofanos());
        model.addAttribute("quirofano", new Quirofano());
        return "quirofanos";
    }

    @PostMapping("/quirofanos/guardar")
    public String guardarQuirofano(@ModelAttribute Quirofano quirofano) {
        gestionService.guardarQuirofano(quirofano);
        return "redirect:/web/solicitudes/quirofanos";
    }

    private String responsableActual(String responsableCapturado, Principal principal) {
        return principal == null ? responsableCapturado : principal.getName();
    }
}
