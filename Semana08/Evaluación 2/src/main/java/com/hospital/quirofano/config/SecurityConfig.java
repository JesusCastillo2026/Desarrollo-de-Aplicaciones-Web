package com.hospital.quirofano.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hospital.quirofano.repository.UsuarioRepository;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RoleLoginSuccessHandler loginSuccessHandler)
            throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login", "/error", "/css/**", "/images/**").permitAll()
                .requestMatchers("/web/admin/**", "/api/usuarios/**", "/api/roles/**", "/api/auditoria/**")
                    .hasRole("ADMINISTRADOR")
                .requestMatchers("/web/solicitudes/*/evaluacion", "/web/solicitudes/*/decision",
                        "/web/solicitudes/*/programar", "/web/solicitudes/programaciones/**",
                        "/web/solicitudes/quirofanos/**", "/api/solicitudes/*/aprobaciones/**",
                        "/api/quirofanos/**")
                    .hasAnyRole("ADMINISTRADOR", "MEDICO")
                .requestMatchers("/api/evaluaciones-preoperatorias/**", "/api/programaciones/**")
                    .hasAnyRole("ADMINISTRADOR", "MEDICO")
                .requestMatchers("/web/solicitudes/**", "/api/solicitudes/**")
                    .hasAnyRole("ADMINISTRADOR", "MEDICO", "RECEPCIONISTA")
                .anyRequest().authenticated())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .permitAll())
            .httpBasic(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll())
            .exceptionHandling(errors -> errors.accessDeniedPage("/acceso-denegado"));
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return nombreUsuario -> usuarioRepository.findWithRolByNombreUsuarioIgnoreCase(nombreUsuario)
                .map(usuario -> User.withUsername(usuario.getNombreUsuario())
                        .password(usuario.getPasswordHash())
                        .roles(usuario.getRol().getNombreRol())
                        .disabled(!usuario.isActivo() || !usuario.getRol().isActivo())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas."));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
