package com.tecsup.ventas.controller;

import com.tecsup.ventas.service.StockInsuficienteException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,String>> noEncontrado(EntityNotFoundException e) { return respuesta(HttpStatus.NOT_FOUND,e.getMessage()); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> datosInvalidos(MethodArgumentNotValidException e) {
        String mensaje=e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField()+": "+error.getDefaultMessage()).collect(Collectors.joining("; "));
        return respuesta(HttpStatus.BAD_REQUEST,mensaje);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, StockInsuficienteException.class})
    public ResponseEntity<Map<String,String>> conflicto(RuntimeException e) { return respuesta(HttpStatus.CONFLICT,e.getMessage()); }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> argumentoInvalido(IllegalArgumentException e) { return respuesta(HttpStatus.BAD_REQUEST,e.getMessage()); }

    private ResponseEntity<Map<String,String>> respuesta(HttpStatus status,String mensaje) {
        return ResponseEntity.status(status).body(Map.of("estado",status.getReasonPhrase(),"mensaje",mensaje==null?"Solicitud no válida":mensaje));
    }
}
