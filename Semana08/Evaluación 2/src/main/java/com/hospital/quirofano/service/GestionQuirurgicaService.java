package com.hospital.quirofano.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hospital.quirofano.audit.AuditOperation;

import com.hospital.quirofano.dto.AprobacionRequest;
import com.hospital.quirofano.dto.EvaluacionRequest;
import com.hospital.quirofano.dto.IntegranteRequest;
import com.hospital.quirofano.dto.ProgramacionRequest;
import com.hospital.quirofano.entity.AprobacionCirugia;
import com.hospital.quirofano.entity.EvaluacionPreoperatoria;
import com.hospital.quirofano.entity.IntegranteEquipo;
import com.hospital.quirofano.entity.Medico;
import com.hospital.quirofano.entity.ProgramacionCirugia;
import com.hospital.quirofano.entity.Quirofano;
import com.hospital.quirofano.entity.SolicitudCirugia;
import com.hospital.quirofano.repository.AprobacionCirugiaRepository;
import com.hospital.quirofano.repository.EvaluacionPreoperatoriaRepository;
import com.hospital.quirofano.repository.IntegranteEquipoRepository;
import com.hospital.quirofano.repository.MedicoRepository;
import com.hospital.quirofano.repository.ProgramacionCirugiaRepository;
import com.hospital.quirofano.repository.QuirofanoRepository;
import com.hospital.quirofano.repository.SolicitudCirugiaRepository;

@Service
public class GestionQuirurgicaService {

    private final EvaluacionPreoperatoriaRepository evaluacionRepository;
    private final AprobacionCirugiaRepository aprobacionRepository;
    private final QuirofanoRepository quirofanoRepository;
    private final ProgramacionCirugiaRepository programacionRepository;
    private final IntegranteEquipoRepository equipoRepository;
    private final SolicitudCirugiaRepository solicitudRepository;
    private final MedicoRepository medicoRepository;

    public GestionQuirurgicaService(
            EvaluacionPreoperatoriaRepository evaluacionRepository,
            AprobacionCirugiaRepository aprobacionRepository,
            QuirofanoRepository quirofanoRepository,
            ProgramacionCirugiaRepository programacionRepository,
            IntegranteEquipoRepository equipoRepository,
            SolicitudCirugiaRepository solicitudRepository,
            MedicoRepository medicoRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.aprobacionRepository = aprobacionRepository;
        this.quirofanoRepository = quirofanoRepository;
        this.programacionRepository = programacionRepository;
        this.equipoRepository = equipoRepository;
        this.solicitudRepository = solicitudRepository;
        this.medicoRepository = medicoRepository;
    }

    public List<EvaluacionPreoperatoria> listarEvaluaciones(Long solicitudId, Long pacienteId) {
        if (solicitudId != null) return evaluacionRepository.findBySolicitud_IdSolicitudOrderByFechaEvaluacionDesc(solicitudId);
        if (pacienteId != null) return evaluacionRepository.findBySolicitud_Paciente_IdPacienteOrderByFechaEvaluacionDesc(pacienteId);
        return evaluacionRepository.findAll();
    }

    public EvaluacionPreoperatoria buscarEvaluacion(Long id) {
        return evaluacionRepository.findById(id).orElseThrow(() -> noEncontrado("Evaluación preoperatoria", id));
    }

    @Transactional
    @AuditOperation(action = "CREAR", entity = "evaluaciones_preoperatorias")
    public EvaluacionPreoperatoria registrarEvaluacion(EvaluacionRequest datos) {
        validarEvaluacion(datos);
        SolicitudCirugia solicitud = buscarSolicitud(datos.solicitudId());
        if ("CANCELADA".equalsIgnoreCase(solicitud.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede evaluar una solicitud cancelada.");
        }
        boolean tieneProgramacionActiva = programacionRepository
                .findBySolicitud_IdSolicitudOrderByFechaHoraInicio(solicitud.getIdSolicitud()).stream()
                .anyMatch(p -> !"CANCELADA".equalsIgnoreCase(p.getEstado()));
        if (tieneProgramacionActiva) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cancele la programación antes de registrar otra evaluación.");
        }
        EvaluacionPreoperatoria evaluacion = new EvaluacionPreoperatoria();
        copiarEvaluacion(evaluacion, datos, solicitud);
        evaluacion.setFechaEvaluacion(LocalDateTime.now());
        solicitud.setEstado("EN_EVALUACION");
        solicitudRepository.save(solicitud);
        return evaluacionRepository.save(evaluacion);
    }

    @Transactional
    @AuditOperation(action = "MODIFICAR", entity = "evaluaciones_preoperatorias")
    public EvaluacionPreoperatoria actualizarEvaluacion(Long id, EvaluacionRequest datos) {
        validarEvaluacion(datos);
        EvaluacionPreoperatoria evaluacion = buscarEvaluacion(id);
        SolicitudCirugia solicitud = datos.solicitudId() == null ? evaluacion.getSolicitud() : buscarSolicitud(datos.solicitudId());
        boolean tieneProgramacionActiva = programacionRepository
                .findBySolicitud_IdSolicitudOrderByFechaHoraInicio(solicitud.getIdSolicitud()).stream()
                .anyMatch(p -> !"CANCELADA".equalsIgnoreCase(p.getEstado()));
        if (tieneProgramacionActiva) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cancele la programación antes de modificar la evaluación.");
        }
        copiarEvaluacion(evaluacion, datos, solicitud);
        evaluacion.setFechaEvaluacion(LocalDateTime.now());
        solicitud.setEstado("EN_EVALUACION");
        solicitudRepository.save(solicitud);
        return evaluacionRepository.save(evaluacion);
    }

    @Transactional
    @AuditOperation(action = "REGISTRAR_DECISION", entity = "aprobaciones_cirugia")
    public AprobacionCirugia registrarDecision(Long solicitudId, AprobacionRequest datos) {
        if (datos == null || textoVacio(datos.estado()) || textoVacio(datos.responsable())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indique estado y responsable de la decisión.");
        }
        String estado = normalizar(datos.estado());
        if (!List.of("APROBADA", "RECHAZADA", "OBSERVADA").contains(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado debe ser APROBADA, RECHAZADA u OBSERVADA.");
        }
        if ("RECHAZADA".equals(estado) && textoVacio(datos.motivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rechazo requiere un motivo.");
        }
        SolicitudCirugia solicitud = buscarSolicitud(solicitudId);
        if ("CANCELADA".equalsIgnoreCase(solicitud.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede decidir sobre una solicitud cancelada.");
        }
        boolean tieneProgramacionActiva = programacionRepository
                .findBySolicitud_IdSolicitudOrderByFechaHoraInicio(solicitudId).stream()
                .anyMatch(p -> !"CANCELADA".equalsIgnoreCase(p.getEstado()));
        if (tieneProgramacionActiva && !"APROBADA".equals(estado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cancele la programación antes de cambiar la aprobación.");
        }
        if ("APROBADA".equals(estado)) {
            EvaluacionPreoperatoria ultima = evaluacionRepository
                    .findTopBySolicitud_IdSolicitudOrderByFechaEvaluacionDesc(solicitudId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Registre primero una evaluación preoperatoria."));
            if (!"APTA".equalsIgnoreCase(ultima.getEstado())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La evaluación preoperatoria más reciente debe estar APTA para aprobar.");
            }
        }

        AprobacionCirugia decision = new AprobacionCirugia();
        decision.setSolicitud(solicitud);
        decision.setEstado(estado);
        decision.setResponsable(datos.responsable().trim());
        decision.setFechaDecision(LocalDateTime.now());
        decision.setObservacion(datos.observacion());
        decision.setMotivo(datos.motivo());
        solicitud.setEstado(estado);
        solicitudRepository.save(solicitud);
        return aprobacionRepository.save(decision);
    }

    public List<AprobacionCirugia> listarDecisiones(Long solicitudId) {
        if (solicitudId == null) return aprobacionRepository.findAll();
        return aprobacionRepository.findBySolicitud_IdSolicitudOrderByFechaDecisionDesc(solicitudId);
    }

    public List<Quirofano> listarQuirofanos() { return quirofanoRepository.findAll(); }

    public List<Quirofano> listarQuirofanosDisponibles(LocalDateTime inicio, Integer duracionMinutos) {
        if (inicio == null || duracionMinutos == null || duracionMinutos < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indique inicio y duración válidos para consultar disponibilidad.");
        }
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);
        List<ProgramacionCirugia> programaciones = programacionRepository.findAll();
        return quirofanoRepository.findAll().stream()
                .filter(q -> "DISPONIBLE".equalsIgnoreCase(q.getEstado()))
                .filter(q -> programaciones.stream()
                        .filter(p -> "PROGRAMADA".equalsIgnoreCase(p.getEstado()))
                        .filter(p -> q.getIdQuirofano().equals(p.getQuirofano().getIdQuirofano()))
                        .noneMatch(p -> inicio.isBefore(p.getFechaHoraFinEstimada()) && p.getFechaHoraInicio().isBefore(fin)))
                .toList();
    }

    public Quirofano buscarQuirofano(Long id) {
        return quirofanoRepository.findById(id).orElseThrow(() -> noEncontrado("Quirófano", id));
    }

    @Transactional
    @AuditOperation(action = "GUARDAR", entity = "quirofanos")
    public Quirofano guardarQuirofano(Quirofano datos) {
        validarQuirofano(datos);
        if (datos.getIdQuirofano() == null && quirofanoRepository.existsByCodigoIgnoreCase(datos.getCodigo().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un quirófano con ese código.");
        }
        if (datos.getIdQuirofano() != null) {
            Quirofano existente = buscarQuirofano(datos.getIdQuirofano());
            if (quirofanoRepository.existsByCodigoIgnoreCaseAndIdQuirofanoNot(datos.getCodigo().trim(), datos.getIdQuirofano())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un quirófano con ese código.");
            }
            existente.setCodigo(datos.getCodigo().trim());
            existente.setNombre(datos.getNombre().trim());
            existente.setArea(datos.getArea());
            existente.setCapacidad(datos.getCapacidad());
            existente.setEquipamiento(datos.getEquipamiento());
            if (!textoVacio(datos.getEstado())) existente.setEstado(normalizar(datos.getEstado()));
            return quirofanoRepository.save(existente);
        }
        datos.setCodigo(datos.getCodigo().trim());
        datos.setNombre(datos.getNombre().trim());
        datos.setEstado(textoVacio(datos.getEstado()) ? "DISPONIBLE" : normalizar(datos.getEstado()));
        return quirofanoRepository.save(datos);
    }

    public List<ProgramacionCirugia> listarProgramaciones(Long solicitudId) {
        if (solicitudId == null) return programacionRepository.findAll();
        return programacionRepository.findBySolicitud_IdSolicitudOrderByFechaHoraInicio(solicitudId);
    }

    public List<ProgramacionCirugia> buscarProgramaciones(Long solicitudId, LocalDateTime desde, LocalDateTime hasta,
            Long quirofanoId, Long cirujanoId) {
        if (desde != null && hasta != null && hasta.isBefore(desde)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha final debe ser posterior a la inicial.");
        }
        return listarProgramaciones(solicitudId).stream()
                .filter(p -> desde == null || !p.getFechaHoraInicio().isBefore(desde))
                .filter(p -> hasta == null || !p.getFechaHoraInicio().isAfter(hasta))
                .filter(p -> quirofanoId == null || quirofanoId.equals(p.getQuirofano().getIdQuirofano()))
                .filter(p -> cirujanoId == null || cirujanoId.equals(p.getCirujano().getIdMedico()))
                .toList();
    }

    public ProgramacionCirugia buscarProgramacion(Long id) {
        return programacionRepository.findById(id).orElseThrow(() -> noEncontrado("Programación", id));
    }

    public List<IntegranteEquipo> listarEquipo(Long programacionId) {
        buscarProgramacion(programacionId);
        return equipoRepository.findByProgramacion_IdProgramacion(programacionId);
    }

    @Transactional
    @AuditOperation(action = "PROGRAMAR", entity = "programaciones_cirugia")
    public ProgramacionCirugia programar(ProgramacionRequest datos) {
        validarProgramacion(datos);
        SolicitudCirugia solicitud = buscarSolicitud(datos.solicitudId());
        validarAutorizacionParaProgramar(solicitud);
        boolean yaProgramada = programacionRepository.findBySolicitud_IdSolicitudOrderByFechaHoraInicio(solicitud.getIdSolicitud())
                .stream().anyMatch(p -> !"CANCELADA".equalsIgnoreCase(p.getEstado()));
        if (yaProgramada) throw new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud ya tiene una programación activa; use reprogramar.");
        ProgramacionCirugia programacion = nuevaProgramacion(datos, solicitud, null);
        comprobarDisponibilidad(programacion, null, datos.equipo());
        programacion = programacionRepository.save(programacion);
        guardarEquipo(programacion, datos.equipo());
        solicitud.setEstado("PROGRAMADA");
        solicitudRepository.save(solicitud);
        return programacion;
    }

    @Transactional
    @AuditOperation(action = "REPROGRAMAR", entity = "programaciones_cirugia")
    public ProgramacionCirugia reprogramar(Long id, ProgramacionRequest datos) {
        validarProgramacion(datos);
        if (textoVacio(datos.motivoCambio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indique el motivo de la reprogramación.");
        }
        ProgramacionCirugia actual = buscarProgramacion(id);
        if ("CANCELADA".equalsIgnoreCase(actual.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede reprogramar una programación cancelada.");
        }
        SolicitudCirugia solicitud = actual.getSolicitud();
        if (datos.solicitudId() != null && !datos.solicitudId().equals(solicitud.getIdSolicitud())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reprogramación debe conservar la solicitud original.");
        }
        actualizarCamposProgramacion(actual, datos);
        actual.setMotivoUltimoCambio(datos.motivoCambio().trim());
        actual.setResponsableUltimoCambio(datos.responsableCambio());
        actual.setFechaUltimoCambio(LocalDateTime.now());
        List<IntegranteRequest> equipoPropuesto = datos.equipo() == null
                ? equipoRepository.findByProgramacion_IdProgramacion(id).stream()
                        .map(persona -> new IntegranteRequest(persona.getMedico().getIdMedico(), persona.getFuncion())).toList()
                : datos.equipo();
        comprobarDisponibilidad(actual, id, equipoPropuesto);
        ProgramacionCirugia guardada = programacionRepository.save(actual);
        if (datos.equipo() != null) {
            equipoRepository.deleteByProgramacion_IdProgramacion(id);
            guardarEquipo(guardada, equipoPropuesto);
        }
        return guardada;
    }

    @Transactional
    @AuditOperation(action = "CANCELAR", entity = "programaciones_cirugia")
    public ProgramacionCirugia cancelarProgramacion(Long id, String responsable, String motivo) {
        if (textoVacio(responsable) || textoVacio(motivo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cancelación requiere responsable y motivo.");
        }
        ProgramacionCirugia programacion = buscarProgramacion(id);
        if ("CANCELADA".equalsIgnoreCase(programacion.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La programación ya está cancelada.");
        }
        programacion.setEstado("CANCELADA");
        programacion.setMotivoUltimoCambio(motivo.trim());
        programacion.setResponsableUltimoCambio(responsable.trim());
        programacion.setFechaUltimoCambio(LocalDateTime.now());
        programacion.getSolicitud().setEstado("CANCELADA");
        solicitudRepository.save(programacion.getSolicitud());
        return programacionRepository.save(programacion);
    }

    private void validarAutorizacionParaProgramar(SolicitudCirugia solicitud) {
        if ("CANCELADA".equalsIgnoreCase(solicitud.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede programar una solicitud cancelada.");
        }
        EvaluacionPreoperatoria evaluacion = evaluacionRepository
                .findTopBySolicitud_IdSolicitudOrderByFechaEvaluacionDesc(solicitud.getIdSolicitud())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Registre la evaluación preoperatoria antes de programar."));
        if (!"APTA".equalsIgnoreCase(evaluacion.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La última evaluación debe estar en estado APTA.");
        }
        AprobacionCirugia aprobacion = aprobacionRepository
                .findTopBySolicitud_IdSolicitudOrderByFechaDecisionDesc(solicitud.getIdSolicitud())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "La solicitud debe contar con una aprobación."));
        if (!"APROBADA".equalsIgnoreCase(aprobacion.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La decisión más reciente debe ser APROBADA.");
        }
    }

    private ProgramacionCirugia nuevaProgramacion(ProgramacionRequest datos, SolicitudCirugia solicitud, Long id) {
        ProgramacionCirugia programacion = id == null ? new ProgramacionCirugia() : buscarProgramacion(id);
        programacion.setSolicitud(solicitud);
        actualizarCamposProgramacion(programacion, datos);
        programacion.setEstado("PROGRAMADA");
        programacion.setMotivoUltimoCambio(datos.motivoCambio());
        programacion.setResponsableUltimoCambio(datos.responsableCambio());
        programacion.setFechaUltimoCambio(LocalDateTime.now());
        return programacion;
    }

    private void actualizarCamposProgramacion(ProgramacionCirugia programacion, ProgramacionRequest datos) {
        Quirofano quirofano = quirofanoRepository.findById(datos.quirofanoId())
                .orElseThrow(() -> noEncontrado("Quirófano", datos.quirofanoId()));
        if (!"DISPONIBLE".equalsIgnoreCase(quirofano.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El quirófano no está disponible para programación.");
        }
        Medico cirujano = medicoRepository.findById(datos.cirujanoId())
                .orElseThrow(() -> noEncontrado("Cirujano", datos.cirujanoId()));
        Medico anestesiologo = medicoRepository.findById(datos.anestesiologoId())
                .orElseThrow(() -> noEncontrado("Anestesiólogo", datos.anestesiologoId()));
        if (cirujano.getIdMedico().equals(anestesiologo.getIdMedico())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cirujano y el anestesiólogo deben ser profesionales distintos.");
        }
        programacion.setQuirofano(quirofano);
        programacion.setCirujano(cirujano);
        programacion.setAnestesiologo(anestesiologo);
        programacion.setFechaHoraInicio(datos.fechaHoraInicio());
        programacion.setFechaHoraFinEstimada(datos.fechaHoraInicio().plusMinutes(datos.duracionEstimadaMinutos()));
        programacion.setDuracionEstimadaMinutos(datos.duracionEstimadaMinutos());
        programacion.setPrioridad(textoVacio(datos.prioridad()) ? programacion.getSolicitud().getPrioridad() : normalizar(datos.prioridad()));
    }

    private void comprobarDisponibilidad(ProgramacionCirugia propuesta, Long excluirId, List<IntegranteRequest> equipoPropuesto) {
        List<ProgramacionCirugia> existentes = programacionRepository.findAll();
        for (ProgramacionCirugia existente : existentes) {
            if ("CANCELADA".equalsIgnoreCase(existente.getEstado()) || existente.getIdProgramacion().equals(excluirId)) continue;
            boolean seCruza = propuesta.getFechaHoraInicio().isBefore(existente.getFechaHoraFinEstimada())
                    && existente.getFechaHoraInicio().isBefore(propuesta.getFechaHoraFinEstimada());
            if (!seCruza) continue;
            if (propuesta.getQuirofano().getIdQuirofano().equals(existente.getQuirofano().getIdQuirofano())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El quirófano ya tiene una cirugía en ese horario.");
            }
            if (mismoProfesional(propuesta.getCirujano(), existente.getCirujano())
                    || mismoProfesional(propuesta.getCirujano(), existente.getAnestesiologo())
                    || mismoProfesional(propuesta.getAnestesiologo(), existente.getCirujano())
                    || mismoProfesional(propuesta.getAnestesiologo(), existente.getAnestesiologo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El cirujano o anestesiólogo ya tiene una cirugía en ese horario.");
            }
            List<IntegranteEquipo> equipoExistente = equipoRepository.findByProgramacion_IdProgramacion(existente.getIdProgramacion());
            for (IntegranteRequest integrante : equipoPropuesto == null ? List.<IntegranteRequest>of() : equipoPropuesto) {
                if (integrante.medicoId().equals(existente.getCirujano().getIdMedico())
                        || integrante.medicoId().equals(existente.getAnestesiologo().getIdMedico())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Un integrante del equipo ya está asignado como profesional principal en otra cirugía a esa hora.");
                }
                if (equipoExistente.stream().anyMatch(e -> e.getMedico().getIdMedico().equals(integrante.medicoId()))) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Un integrante del equipo ya está asignado en otra cirugía a esa hora.");
                }
            }
            if (equipoExistente.stream().anyMatch(e -> e.getMedico().getIdMedico().equals(propuesta.getCirujano().getIdMedico())
                    || e.getMedico().getIdMedico().equals(propuesta.getAnestesiologo().getIdMedico()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Un profesional principal ya está asignado al equipo de otra cirugía en ese horario.");
            }
        }
    }

    private void guardarEquipo(ProgramacionCirugia programacion, List<IntegranteRequest> equipo) {
        if (equipo == null) return;
        java.util.Set<Long> ids = new java.util.HashSet<>();
        for (IntegranteRequest datos : equipo) {
            if (datos == null || datos.medicoId() == null || textoVacio(datos.funcion())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada integrante requiere médico y función.");
            }
            if (!ids.add(datos.medicoId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No repita profesionales en el equipo.");
            if (datos.medicoId().equals(programacion.getCirujano().getIdMedico())
                    || datos.medicoId().equals(programacion.getAnestesiologo().getIdMedico())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cirujano y anestesiólogo se registran en sus campos propios.");
            }
            Medico medico = medicoRepository.findById(datos.medicoId())
                    .orElseThrow(() -> noEncontrado("Integrante del equipo", datos.medicoId()));
            IntegranteEquipo integrante = new IntegranteEquipo();
            integrante.setProgramacion(programacion);
            integrante.setMedico(medico);
            integrante.setFuncion(datos.funcion().trim());
            equipoRepository.save(integrante);
        }
    }

    private void validarEvaluacion(EvaluacionRequest datos) {
        if (datos == null || datos.solicitudId() == null || datos.profesionalId() == null || textoVacio(datos.estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitud, profesional y estado de evaluación son obligatorios.");
        }
        if (!List.of("APTA", "OBSERVADA", "NO_APTA").contains(normalizar(datos.estado()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado debe ser APTA, OBSERVADA o NO_APTA.");
        }
        if (!solicitudRepository.existsById(datos.solicitudId())) throw noEncontrado("Solicitud", datos.solicitudId());
        if (!medicoRepository.existsById(datos.profesionalId())) throw noEncontrado("Profesional", datos.profesionalId());
    }

    private void copiarEvaluacion(EvaluacionPreoperatoria evaluacion, EvaluacionRequest datos, SolicitudCirugia solicitud) {
        evaluacion.setSolicitud(solicitud);
        evaluacion.setProfesional(medicoRepository.findById(datos.profesionalId())
                .orElseThrow(() -> noEncontrado("Profesional", datos.profesionalId())));
        evaluacion.setAntecedentesRelevantes(datos.antecedentesRelevantes());
        evaluacion.setAlergias(datos.alergias());
        evaluacion.setMedicacionActual(datos.medicacionActual());
        evaluacion.setObservaciones(datos.observaciones());
        evaluacion.setEstado(normalizar(datos.estado()));
    }

    private void validarProgramacion(ProgramacionRequest datos) {
        if (datos == null || datos.solicitudId() == null || datos.quirofanoId() == null || datos.cirujanoId() == null
                || datos.anestesiologoId() == null || datos.fechaHoraInicio() == null || datos.duracionEstimadaMinutos() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitud, quirófano, cirujano, anestesiólogo, inicio y duración son obligatorios.");
        }
        if (datos.duracionEstimadaMinutos() < 1 || datos.duracionEstimadaMinutos() > 1440) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La duración debe ser entre 1 y 1440 minutos.");
        }
        if (datos.fechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de programación debe ser futura.");
        }
        if (datos.equipo() != null && datos.equipo().stream()
                .anyMatch(persona -> persona == null || persona.medicoId() == null || textoVacio(persona.funcion()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada integrante del equipo requiere médico y función.");
        }
    }

    private void validarQuirofano(Quirofano datos) {
        if (datos == null || textoVacio(datos.getCodigo()) || textoVacio(datos.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código y nombre del quirófano son obligatorios.");
        }
        if (datos.getCapacidad() != null && datos.getCapacidad() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La capacidad debe ser mayor a cero.");
        }
        if (!textoVacio(datos.getEstado()) && !List.of("DISPONIBLE", "RESERVADO", "EN_USO", "MANTENIMIENTO", "FUERA_DE_SERVICIO").contains(normalizar(datos.getEstado()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de quirófano no válido.");
        }
    }

    private SolicitudCirugia buscarSolicitud(Long id) {
        if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador de solicitud es obligatorio.");
        return solicitudRepository.findById(id).orElseThrow(() -> noEncontrado("Solicitud", id));
    }

    private ResponseStatusException noEncontrado(String recurso, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, recurso + " no encontrado: " + id);
    }

    private boolean mismoProfesional(Medico a, Medico b) {
        return a != null && b != null && a.getIdMedico().equals(b.getIdMedico());
    }

    private boolean textoVacio(String valor) { return valor == null || valor.isBlank(); }
    private String normalizar(String valor) { return valor.trim().toUpperCase(Locale.ROOT).replace(' ', '_'); }
}
