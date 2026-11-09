package com.tecsup.pedido.client;

import com.tecsup.pedido.dto.ProductoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service")
public interface ProductoClient {
    @GetMapping("/api/productos/{id}")
    ProductoResponse obtenerProducto(@PathVariable("id") Long id);
}
