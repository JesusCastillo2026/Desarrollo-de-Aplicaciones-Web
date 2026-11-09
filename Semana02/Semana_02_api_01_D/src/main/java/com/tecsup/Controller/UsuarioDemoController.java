package com.tecsup.Controller;

import com.tecsup.Model.Usuario;
import com.tecsup.Services.UsuarioDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioDemoController {

    private final UsuarioDemoService usuarioDemoService;

    public UsuarioDemoController(UsuarioDemoService usuarioDemoService) {
        this.usuarioDemoService = usuarioDemoService;
    }

    @GetMapping("/usuario")
    public Usuario obtenerUsuario() {
        return usuarioDemoService.obtenerUsuario();
    }
}
