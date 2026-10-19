package com.hospital.quirofano.config;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hospital.quirofano.entity.Rol;
import com.hospital.quirofano.entity.Usuario;
import com.hospital.quirofano.repository.RolRepository;
import com.hospital.quirofano.repository.UsuarioRepository;

@Configuration
@EnableConfigurationProperties(BootstrapAdminProperties.class)
public class DatosInicialesConfig {

    @Bean
    CommandLineRunner inicializarSeguridad(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            BootstrapAdminProperties bootstrapAdmin) {
        return args -> {
            crearRolSiNoExiste(rolRepository, "ADMINISTRADOR", "Administra usuarios, roles y el sistema.");
            crearRolSiNoExiste(rolRepository, "MEDICO", "Gestiona evaluaciones y programación quirúrgica.");
            crearRolSiNoExiste(rolRepository, "RECEPCIONISTA", "Registra y consulta solicitudes quirúrgicas.");

            if (usuarioRepository.findByNombreUsuarioIgnoreCase(bootstrapAdmin.getUsername()).isEmpty()) {
                Rol rolAdmin = rolRepository.findByNombreRolIgnoreCase("ADMINISTRADOR")
                        .orElseThrow(() -> new IllegalStateException("No se pudo crear el rol administrador."));
                Usuario admin = new Usuario();
                admin.setNombreUsuario(bootstrapAdmin.getUsername().trim().toLowerCase(Locale.ROOT));
                admin.setCorreo(bootstrapAdmin.getEmail().trim().toLowerCase(Locale.ROOT));
                admin.setNombres("Administrador");
                admin.setApellidos("del sistema");
                admin.setPasswordHash(passwordEncoder.encode(bootstrapAdmin.getPassword()));
                admin.setRol(rolAdmin);
                admin.setActivo(true);
                usuarioRepository.save(admin);
            }
        };
    }

    private void crearRolSiNoExiste(RolRepository roles, String nombre, String descripcion) {
        if (roles.findByNombreRolIgnoreCase(nombre).isPresent()) return;
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        rol.setDescripcion(descripcion);
        rol.setActivo(true);
        roles.save(rol);
    }
}
