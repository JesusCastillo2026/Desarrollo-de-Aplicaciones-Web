package com.tecsup.cliente.controller;
import com.tecsup.cliente.dto.ClienteRequest;
import com.tecsup.cliente.model.Cliente;
import com.tecsup.cliente.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/clientes")
public class ClienteController {
 private final ClienteService service;
 public ClienteController(ClienteService service) { this.service=service; }
 @GetMapping public List<Cliente> listar() { return service.listar(); }
 @GetMapping("/{id}") public Cliente obtener(@PathVariable Long id) { return service.obtener(id); }
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Cliente crear(@Valid @RequestBody ClienteRequest dto) { return service.crear(dto); }
 @PutMapping("/{id}") public Cliente actualizar(@PathVariable Long id,@Valid @RequestBody ClienteRequest dto) { return service.actualizar(id,dto); }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
