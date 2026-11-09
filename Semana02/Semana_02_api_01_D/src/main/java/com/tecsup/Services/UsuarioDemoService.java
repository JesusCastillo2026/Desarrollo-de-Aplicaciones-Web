package com.tecsup.Services;

import com.tecsup.Model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDemoService {

    public Usuario obtenerUsuario() {
        return new Usuario("Ricardo", "ricardo@email.com");
    }
}
