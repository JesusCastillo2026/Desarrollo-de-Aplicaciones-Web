package com.tecsup.aspect;

import com.tecsup.exception.ForbiddenException;
import com.tecsup.exception.UnauthorizedException;
import com.tecsup.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Aspect
@Component
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;
    private final HttpServletRequest request;

    // Inyección por constructor (incluimos el request para leer los Headers de Postman)
    public AuditoriaAspect(AuditoriaService auditoriaService, HttpServletRequest request) {
        this.auditoriaService = auditoriaService;
        this.request = request;
    }

    // --- ACTIVIDAD: Permitir que USER también pueda crear productos ---
    @Before("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void verificarPermisosCrear(JoinPoint joinPoint) {
        String rol = request.getHeader("Rol");
        
        if (rol == null || rol.isEmpty()) {
            throw new UnauthorizedException("Falta indicar el Rol en los Headers");
        }
        
        // Aceptamos tanto ADMIN como USER
        if (!rol.equalsIgnoreCase("ADMIN") && !rol.equalsIgnoreCase("USER")) {
            throw new ForbiddenException("El rol " + rol + " no tiene permisos para crear productos");
        }
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void auditarGuardar(JoinPoint joinPoint) {
        auditoriaService.registrar("CREAR", joinPoint.getSignature().getName(), "Se registró un producto");
    }

    // --- ACTIVIDAD: Mejorar auditoría en eliminar() ---
    @AfterReturning("execution(* com.tecsup.service.ProductoService.eliminar(..))")
    public void auditarEliminar(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (args.length > 0) ? (Long) args[0] : null;

        auditoriaService.registrar(
                "ELIMINAR",
                joinPoint.getSignature().getName(),
                "Se eliminó producto ID: " + id // Texto exacto según tu imagen
        );
    }

    // ACTUALIZAR 
    @AfterReturning("execution(* com.tecsup.controller.ProductoController.actualizar(..))")
    public void auditarActualizar(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (args.length > 0) ? (Long) args[0] : null;
        auditoriaService.registrar("ACTUALIZAR", joinPoint.getSignature().getName(), "Se actualizó producto con ID: " + id);
    }

    // --- ACTIVIDAD: Registrar auditoría en listar() ---
    @AfterReturning(pointcut = "execution(* com.tecsup.service.ProductoService.listar(..))", returning = "lista")
    public void auditarListar(JoinPoint joinPoint, Object lista) {
        int cantidad = 0;
        if (lista instanceof List) {
            cantidad = ((List<?>) lista).size();
        }

        auditoriaService.registrar(
                "LISTAR",
                joinPoint.getSignature().getName(),
                "Cantidad de productos: " + cantidad // Texto exacto según tu imagen
        );
    }
}