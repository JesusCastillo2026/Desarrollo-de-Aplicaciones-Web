package com.hospital.quirofano.audit;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hospital.quirofano.entity.AuditoriaOperacion;
import com.hospital.quirofano.service.AuditoriaService;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuditAspect {

    private final AuditoriaService auditoriaService;

    public AuditAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @AfterReturning(pointcut = "@annotation(operation)", returning = "result")
    public void registrarOperacion(JoinPoint joinPoint, AuditOperation operation, Object result) {
        AuditoriaOperacion auditoria = new AuditoriaOperacion();
        auditoria.setUsuario(usuarioActual());
        auditoria.setOperacion(operation.action());
        auditoria.setEntidad(operation.entity());
        auditoria.setRegistroId(identificador(result, joinPoint.getArgs()));
        auditoria.setDetalle(joinPoint.getSignature().getName());
        auditoriaService.registrar(auditoria);
    }

    private String usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "SISTEMA";
        }
        return authentication.getName();
    }

    private String identificador(Object resultado, Object[] argumentos) {
        String idResultado = buscarId(resultado);
        if (idResultado != null) return idResultado;
        return Arrays.stream(argumentos)
                .map(this::buscarId)
                .filter(id -> id != null)
                .findFirst()
                .orElse(null);
    }

    private String buscarId(Object valor) {
        if (valor == null) return null;
        if (valor instanceof ResponseEntity<?> response) return buscarId(response.getBody());
        if (valor instanceof Number number) return number.toString();

        for (Method method : valor.getClass().getMethods()) {
            String nombre = method.getName();
            if (method.getParameterCount() != 0 || !Number.class.isAssignableFrom(method.getReturnType())) continue;
            boolean esPropiedadId = nombre.startsWith("getId")
                    || nombre.matches("id[A-Z].*");
            if (!esPropiedadId) continue;
            try {
                Object id = method.invoke(valor);
                if (id != null) return id.toString();
            } catch (ReflectiveOperationException ignored) {
                // Sigue buscando otra propiedad de identificador que pueda exponerse.
            }
        }
        return null;
    }
}
