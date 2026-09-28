package com.tecsup.service;

import com.tecsup.model.AuditoriaLog;
import com.tecsup.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {    
    
    private final AuditoriaRepository repo; 
    
    // Inyección por constructor (sin @Autowired)
    public AuditoriaService(AuditoriaRepository repo) {
        this.repo = repo;
    }
    
    // Método configurado estrictamente para 3 parámetros
    public void registrar(String accion, String metodo, String detalle) {        
        AuditoriaLog log = new AuditoriaLog(accion, metodo, detalle);        
        repo.save(log);    
    }
}