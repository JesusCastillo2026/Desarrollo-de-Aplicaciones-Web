package com.tecsup.restaurante.pedido.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ServicioRemotoConfig {
    @Bean
    RestClient clienteRestClient(@Value("${rest.services.cliente-base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    RestClient platoRestClient(@Value("${rest.services.plato-base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
