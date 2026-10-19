package com.hospital.quirofano.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.quirofano.entity.AuditoriaOperacion;
import com.hospital.quirofano.repository.AuditoriaOperacionRepository;

@Service
public class AuditoriaService {

    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");
    private final AuditoriaOperacionRepository auditoriaRepository;

    public AuditoriaService(AuditoriaOperacionRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(AuditoriaOperacion auditoria) {
        auditoria.setFechaHora(LocalDateTime.now(ZONA_PERU));
        auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaOperacion> listarRecientes() {
        return auditoriaRepository.findTop200ByOrderByFechaHoraDesc();
    }
}
