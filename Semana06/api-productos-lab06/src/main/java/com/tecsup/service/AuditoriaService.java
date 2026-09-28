package com.tecsup.service;

import com.tecsup.model.AuditoriaLog;
import com.tecsup.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {    
    
    private final AuditoriaRepository repo; // Ya no lleva @Autowired
    
    // Usamos el constructor para inyectar el repositorio
    public AuditoriaService(AuditoriaRepository repo) {
        this.repo = repo;
    }
    
    public void registrar(String accion, String metodo, String detalle) {        
        AuditoriaLog log = new AuditoriaLog(accion, metodo, detalle);        
        repo.save(log);    
    }
}