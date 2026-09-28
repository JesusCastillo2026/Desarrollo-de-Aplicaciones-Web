package com.hospital.quirofano.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.hospital.quirofano.entity.HistoriaClinica;
import com.hospital.quirofano.entity.Medico;
import com.hospital.quirofano.entity.Paciente;
import com.hospital.quirofano.entity.SolicitudCirugia;
import com.hospital.quirofano.repository.HistoriaClinicaRepository;
import com.hospital.quirofano.repository.MedicoRepository;
import com.hospital.quirofano.repository.PacienteRepository;
import com.hospital.quirofano.repository.SolicitudCirugiaRepository;

@Service
public class SolicitudCirugiaService {

    private final SolicitudCirugiaRepository solicitudRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final HistoriaClinicaRepository historiaRepository;

    public SolicitudCirugiaService(
            SolicitudCirugiaRepository solicitudRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            HistoriaClinicaRepository historiaRepository) {
        this.solicitudRepository = solicitudRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.historiaRepository = historiaRepository;
    }

    public List<SolicitudCirugia> listarTodas() {
        return solicitudRepository.findAll();
    }

    public List<SolicitudCirugia> listarPorEstado(String estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public Optional<SolicitudCirugia> buscarPorId(Long id) {
        return solicitudRepository.findById(id);
    }

    public SolicitudCirugia registrarSolicitud(SolicitudCirugia solicitud) {
        if (solicitud.getEstado() == null || solicitud.getEstado().isEmpty()) {
            solicitud.setEstado("SOLICITADA");
        }
        return solicitudRepository.save(solicitud);
    }

    public SolicitudCirugia actualizarSolicitud(Long id, SolicitudCirugia datos) {
        return solicitudRepository.findById(id).map(s -> {
            s.setProcedimiento(datos.getProcedimiento());
            s.setDiagnostico(datos.getDiagnostico());
            s.setMotivo(datos.getMotivo());
            s.setPrioridad(datos.getPrioridad());
            s.setObservaciones(datos.getObservaciones());
            s.setOrigen(datos.getOrigen());
            if (datos.getPaciente() != null) s.setPaciente(datos.getPaciente());
            if (datos.getMedicoSolicitante() != null) s.setMedicoSolicitante(datos.getMedicoSolicitante());
            if (datos.getHistoriaClinica() != null) s.setHistoriaClinica(datos.getHistoriaClinica());
            return solicitudRepository.save(s);
        }).orElse(null);
    }

    public SolicitudCirugia cancelarSolicitud(Long id, String motivo) {
        return solicitudRepository.findById(id).map(s -> {
            s.setEstado("CANCELADA");
            s.setMotivo(motivo);
            return solicitudRepository.save(s);
        }).orElse(null);
    }

    public boolean eliminarSolicitud(Long id) {
        if (solicitudRepository.existsById(id)) {
            solicitudRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Medico guardarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public List<HistoriaClinica> listarHistorias() {
        return historiaRepository.findAll();
    }
}