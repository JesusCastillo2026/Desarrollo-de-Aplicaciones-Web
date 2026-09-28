package com.hospital.quirofano.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.quirofano.entity.HistoriaClinica;
import com.hospital.quirofano.entity.Medico;
import com.hospital.quirofano.entity.Paciente;
import com.hospital.quirofano.entity.SolicitudCirugia;
import com.hospital.quirofano.repository.HistoriaClinicaRepository;
import com.hospital.quirofano.service.SolicitudCirugiaService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudCirugiaController {

    private final SolicitudCirugiaService solicitudService;
    private final HistoriaClinicaRepository historiaRepository;

    public SolicitudCirugiaController(SolicitudCirugiaService solicitudService, HistoriaClinicaRepository historiaRepository) {
        this.solicitudService = solicitudService;
        this.historiaRepository = historiaRepository;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCirugia>> listarTodas(@RequestParam(required = false) String estado) {
        if (estado != null && !estado.isEmpty()) {
            return ResponseEntity.ok(solicitudService.listarPorEstado(estado));
        }
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCirugia> buscarPorId(@PathVariable Long id) {
        return solicitudService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitudCirugia> registrar(@RequestBody SolicitudCirugia solicitud) {
        return ResponseEntity.ok(solicitudService.registrarSolicitud(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCirugia> actualizar(@PathVariable Long id, @RequestBody SolicitudCirugia datos) {
        SolicitudCirugia actualizada = solicitudService.actualizarSolicitud(id, datos);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<SolicitudCirugia> cancelar(@PathVariable Long id, @RequestParam String motivo) {
        SolicitudCirugia cancelada = solicitudService.cancelarSolicitud(id, motivo);
        if (cancelada != null) {
            return ResponseEntity.ok(cancelada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (solicitudService.eliminarSolicitud(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(solicitudService.guardarPaciente(paciente));
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(solicitudService.listarPacientes());
    }

    @PostMapping("/medicos")
    public ResponseEntity<Medico> registrarMedico(@RequestBody Medico medico) {
        return ResponseEntity.ok(solicitudService.guardarMedico(medico));
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> listarMedicos() {
        return ResponseEntity.ok(solicitudService.listarMedicos());
    }

    @PostMapping("/historias")
    public ResponseEntity<HistoriaClinica> registrarHistoria(@RequestBody HistoriaClinica historia) {
        return ResponseEntity.ok(historiaRepository.save(historia));
    }
}