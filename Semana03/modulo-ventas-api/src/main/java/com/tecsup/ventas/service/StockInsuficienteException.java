package com.tecsup.ventas.service;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String mensaje) { super(mensaje); }
}
