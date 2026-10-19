package com.hospital.quirofano.controller;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> manejarEstado(ResponseStatusException exception, HttpServletRequest request) {
        HttpStatusCode status = exception.getStatusCode();
        String message = exception.getReason() == null ? "La solicitud no pudo procesarse." : exception.getReason();
        return respuesta(status, message, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException exception, HttpServletRequest request) {
        FieldError error = exception.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String message = error == null ? "Revise los datos enviados." : error.getField() + ": " + error.getDefaultMessage();
        return respuesta(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJson(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo JSON está vacío o tiene un formato no válido.", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, "La operación incumple una restricción de integridad o un dato único.", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> manejarAcceso(AccessDeniedException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.FORBIDDEN, "El perfil autenticado no tiene permiso para este recurso.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInterno(Exception exception, HttpServletRequest request) {
        log.error("Error inesperado atendiendo {} {}", request.getMethod(), request.getRequestURI(), exception);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno al procesar la solicitud.", request);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatusCode status, String message, HttpServletRequest request) {
        ApiError body = new ApiError(Instant.now(), status.value(), message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }

    public record ApiError(Instant timestamp, int status, String message, String path) {}
}
