package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Aspect
@Component
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;

    // Inyección de dependencias por constructor (Buena práctica)
    public AuditoriaAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    // --- PARTE 1: CREAR ---
    @AfterReturning("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void auditarGuardar(JoinPoint joinPoint) {
        auditoriaService.registrar(
                "CREAR",
                joinPoint.getSignature().getName(),
                "Se registró un producto"
        );
    }

    // --- PARTE 2: ACTUALIZAR (Mensaje Dinámico con JoinPoint) ---
    @AfterReturning("execution(* com.tecsup.controller.ProductoController.actualizar(..))")
    public void auditarActualizar(JoinPoint joinPoint) {
        // Capturamos los parámetros del método. El ID suele ser el primer parámetro (posición 0)
        Object[] args = joinPoint.getArgs();
        Long id = (args.length > 0) ? (Long) args[0] : null;

        auditoriaService.registrar(
                "ACTUALIZAR",
                joinPoint.getSignature().getName(),
                "Se actualizó producto con ID: " + id // Cumple con el ejemplo esperado
        );
    }

    // --- PARTE 2: ELIMINAR (Mensaje Dinámico con JoinPoint) ---
    @AfterReturning("execution(* com.tecsup.service.ProductoService.eliminar(..))")
    public void auditarEliminar(JoinPoint joinPoint) {
        // Capturamos el ID del producto eliminado
        Object[] args = joinPoint.getArgs();
        Long id = (args.length > 0) ? (Long) args[0] : null;

        auditoriaService.registrar(
                "ELIMINAR",
                joinPoint.getSignature().getName(),
                "Se eliminó producto con ID: " + id
        );
    }

    // --- PARTE 1 y 2: LISTAR (Mensaje Dinámico capturando el resultado) ---
    @AfterReturning(pointcut = "execution(* com.tecsup.service.ProductoService.listar(..))", returning = "lista")
    public void auditarListar(JoinPoint joinPoint, Object lista) {
        int cantidad = 0;
        // Verificamos si lo que devolvió el método es una lista para contar su tamaño
        if (lista instanceof List) {
            cantidad = ((List<?>) lista).size();
        }

        auditoriaService.registrar(
                "LISTAR",
                joinPoint.getSignature().getName(),
                "Se obtuvieron " + cantidad + " registros"
        );
    }
}