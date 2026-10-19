package com.hospital.quirofano.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RoleLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String destino = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMINISTRADOR".equals(authority.getAuthority()))
                        ? "/web/admin/usuarios"
                        : "/web/solicitudes/home";
        response.sendRedirect(request.getContextPath() + destino);
    }
}
