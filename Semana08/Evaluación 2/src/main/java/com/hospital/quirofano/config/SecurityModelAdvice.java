package com.hospital.quirofano.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class SecurityModelAdvice {

    @ModelAttribute
    public void agregarPermisosAlModelo(Model model, Authentication authentication) {
        boolean autenticado = authentication != null && authentication.isAuthenticated();
        model.addAttribute("usuarioActual", autenticado ? authentication.getName() : "");
        model.addAttribute("esAdministrador", tieneRol(authentication, "ROLE_ADMINISTRADOR"));
        model.addAttribute("esMedico", tieneRol(authentication, "ROLE_MEDICO"));
        model.addAttribute("esRecepcionista", tieneRol(authentication, "ROLE_RECEPCIONISTA"));
    }

    private boolean tieneRol(Authentication authentication, String rol) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> rol.equals(authority.getAuthority()));
    }
}
