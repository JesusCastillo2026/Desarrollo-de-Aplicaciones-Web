package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {

    private final AuditoriaService auditoriaService;

    // Inyección de dependencias por constructor
    public ErrorAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @AfterThrowing(
            pointcut = "execution(* com.tecsup.service.*.*(..))",
            throwing = "ex"
    )
    public void capturarError(Exception ex) {
        // 1. Mantiene el comportamiento original de mostrar el error en consola
        System.out.println("ERROR AOP: " + ex.getMessage());

        // 2. PARTE 3: Registra dinámicamente el error en la base de datos
        auditoriaService.registrar(
                "ERROR",
                "Fallo en el sistema",
                ex.getMessage() // Captura el mensaje exacto de la excepción
        );
    }
}