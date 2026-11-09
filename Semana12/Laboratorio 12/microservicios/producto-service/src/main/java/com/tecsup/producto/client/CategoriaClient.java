package com.tecsup.producto.client;

import com.tecsup.producto.dto.CategoriaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "categoria-service")
public interface CategoriaClient {
    @GetMapping("/api/categorias/{id}")
    CategoriaResponse obtenerCategoria(@PathVariable("id") Long id);
}
