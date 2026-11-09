package com.tecsup.pedido.client;

import com.tecsup.pedido.dto.ClienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service")
public interface ClienteClient {
    @GetMapping("/api/clientes/{id}")
    ClienteResponse obtenerCliente(@PathVariable("id") Long id);
}
