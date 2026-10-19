package com.hospital.quirofano.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.quirofano.entity.SolicitudCirugia;

public interface SolicitudCirugiaRepository extends JpaRepository<SolicitudCirugia, Long> {
    List<SolicitudCirugia> findByEstado(String estado);
}