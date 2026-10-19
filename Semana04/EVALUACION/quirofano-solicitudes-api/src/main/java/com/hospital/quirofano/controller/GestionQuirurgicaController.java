package com.hospital.quirofano.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.quirofano.dto.AprobacionRequest;
import com.hospital.quirofano.dto.DecisionRequest;
import com.hospital.quirofano.dto.EvaluacionRequest;
import com.hospital.quirofano.dto.ProgramacionRequest;
import com.hospital.quirofano.entity.AprobacionCirugia;
import com.hospital.quirofano.entity.EvaluacionPreoperatoria;
import com.hospital.quirofano.entity.IntegranteEquipo;
import com.hospital.quirofano.entity.ProgramacionCirugia;
import com.hospital.quirofano.entity.Quirofano;
import com.hospital.quirofano.service.GestionQuirurgicaService;

@RestController
@RequestMapping("/api")
public class GestionQuirurgicaController {

    private final GestionQuirurgicaService gestionService;

    public GestionQuirurgicaController(GestionQuirurgicaService gestionService) {
        this.gestionService = gestionService;
    }

    @GetMapping("/evaluaciones-preoperatorias")
    public List<EvaluacionPreoperatoria> listarEvaluaciones(
            @RequestParam(required = false) Long solicitudId,
            @RequestParam(required = false) Long pacienteId) {
        return gestionService.listarEvaluaciones(solicitudId, pacienteId);
    }

    @GetMapping("/evaluaciones-preoperatorias/{id}")
    public EvaluacionPreoperatoria buscarEvaluacion(@PathVariable Long id) {
        return gestionService.buscarEvaluacion(id);
    }

    @PostMapping("/evaluaciones-preoperatorias")
    public ResponseEntity<EvaluacionPreoperatoria> registrarEvaluacion(@RequestBody EvaluacionRequest datos) {
        return ResponseEntity.ok(gestionService.registrarEvaluacion(datos));
    }

    @PutMapping("/evaluaciones-preoperatorias/{id}")
    public EvaluacionPreoperatoria actualizarEvaluacion(@PathVariable Long id, @RequestBody EvaluacionRequest datos) {
        return gestionService.actualizarEvaluacion(id, datos);
    }

    @GetMapping("/solicitudes/{id}/aprobaciones")
    public List<AprobacionCirugia> listarAprobaciones(@PathVariable Long id) {
        return gestionService.listarDecisiones(id);
    }

    @PostMapping("/solicitudes/{id}/aprobaciones")
    public ResponseEntity<AprobacionCirugia> registrarDecision(@PathVariable Long id, @RequestBody AprobacionRequest datos,
            Principal principal) {
        AprobacionRequest decision = principal == null ? datos
                : new AprobacionRequest(datos.estado(), principal.getName(), datos.observacion(), datos.motivo());
        return ResponseEntity.ok(gestionService.registrarDecision(id, decision));
    }

    @GetMapping("/quirofanos")
    public List<Quirofano> listarQuirofanos() { return gestionService.listarQuirofanos(); }

    @GetMapping("/quirofanos/disponibles")
    public List<Quirofano> listarQuirofanosDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam Integer duracionMinutos) {
        return gestionService.listarQuirofanosDisponibles(inicio, duracionMinutos);
    }

    @GetMapping("/quirofanos/{id}")
    public Quirofano buscarQuirofano(@PathVariable Long id) { return gestionService.buscarQuirofano(id); }

    @PostMapping("/quirofanos")
    public ResponseEntity<Quirofano> registrarQuirofano(@RequestBody Quirofano datos) {
        return ResponseEntity.ok(gestionService.guardarQuirofano(datos));
    }

    @PutMapping("/quirofanos/{id}")
    public Quirofano actualizarQuirofano(@PathVariable Long id, @RequestBody Quirofano datos) {
        datos.setIdQuirofano(id);
        return gestionService.guardarQuirofano(datos);
    }

    @GetMapping("/programaciones")
    public List<ProgramacionCirugia> listarProgramaciones(
            @RequestParam(required = false) Long solicitudId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false) Long quirofanoId,
            @RequestParam(required = false) Long cirujanoId) {
        return gestionService.buscarProgramaciones(solicitudId, desde, hasta, quirofanoId, cirujanoId);
    }

    @GetMapping("/programaciones/{id}")
    public ProgramacionCirugia buscarProgramacion(@PathVariable Long id) { return gestionService.buscarProgramacion(id); }

    @PostMapping("/programaciones")
    public ResponseEntity<ProgramacionCirugia> programar(@RequestBody ProgramacionRequest datos) {
        return ResponseEntity.ok(gestionService.programar(datos));
    }

    @PutMapping("/programaciones/{id}/reprogramar")
    public ProgramacionCirugia reprogramar(@PathVariable Long id, @RequestBody ProgramacionRequest datos) {
        return gestionService.reprogramar(id, datos);
    }

    @PutMapping("/programaciones/{id}/cancelar")
    public ProgramacionCirugia cancelarProgramacion(@PathVariable Long id, @RequestBody DecisionRequest datos, Principal principal) {
        String responsable = principal == null ? datos.responsable() : principal.getName();
        return gestionService.cancelarProgramacion(id, responsable, datos.motivo());
    }

    @GetMapping("/programaciones/{id}/equipo")
    public List<IntegranteEquipo> listarEquipo(@PathVariable Long id) { return gestionService.listarEquipo(id); }
}
