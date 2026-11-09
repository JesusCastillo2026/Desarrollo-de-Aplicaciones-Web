package com.tecsup.ventas.controller;

import com.tecsup.ventas.dto.ClienteRequest;
import com.tecsup.ventas.model.Cliente;
import com.tecsup.ventas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service=service; }
    @GetMapping public List<Cliente> listar() { return service.listar(); }
    @GetMapping("/{id}") public Cliente obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Cliente crear(@Valid @RequestBody ClienteRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest r) { return service.actualizar(id,r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
