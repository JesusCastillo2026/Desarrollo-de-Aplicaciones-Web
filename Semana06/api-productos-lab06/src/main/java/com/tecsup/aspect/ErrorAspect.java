package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {

    private final AuditoriaService auditoriaService;

    // Inyección por constructor
    public ErrorAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @AfterThrowing(
            pointcut = "execution(* com.tecsup.service.*.*(..))",
            throwing = "ex"
    )
    public void capturarError(Exception ex) {
        // 1. Muestra el error original en consola
        System.out.println("ERROR AOP: " + ex.getMessage());

        // 2. Registra el error en la base de datos (enviando 3 parámetros)
        auditoriaService.registrar(
                "ERROR",
                "Fallo en el sistema",
                ex.getMessage() 
        );
    }
}