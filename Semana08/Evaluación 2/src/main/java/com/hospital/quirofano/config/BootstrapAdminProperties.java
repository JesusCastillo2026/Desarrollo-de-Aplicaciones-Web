package com.hospital.quirofano.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuración tipada de la cuenta administradora inicial del laboratorio. */
@ConfigurationProperties(prefix = "app.security.bootstrap-admin")
public class BootstrapAdminProperties {

    private String username = "admin";
    private String email = "admin@hospital.local";
    private String password = "Admin123!";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
