package com.tecsup.Controller;

import com.tecsup.Services.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/vista")
    public String verProductos(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "productos";
    }
}

