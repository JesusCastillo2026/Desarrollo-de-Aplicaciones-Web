package com.tecsup.empleados.controller;

import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfiguracionController {
    private final Environment environment;

    @Value("${empresa.ambiente}")
    private String ambiente;

    @Value("${server.port}")
    private String puerto;

    @Value("${spring.datasource.url}")
    private String urlBaseDatos;

    public ConfiguracionController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/configuracion")
    public Map<String, String> obtenerConfiguracion() {
        String[] perfiles = environment.getActiveProfiles();
        String perfil = perfiles.length == 0 ? "default" : perfiles[0];
        String baseDatos = URI.create(urlBaseDatos.substring("jdbc:".length())).getPath();
        baseDatos = baseDatos == null ? "" : baseDatos.replaceFirst("^/", "");
        return Map.of("ambiente", ambiente, "perfil", perfil, "puerto", puerto,
                "baseDatos", baseDatos);
    }
}
