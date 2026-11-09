package com.tecsup.Config;

import com.tecsup.Model.Producto;
import com.tecsup.Model.Usuario;
import com.tecsup.Repository.ProductoRepository;
import com.tecsup.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDeEjemplo(UsuarioRepository usuarioRepository,
                                            ProductoRepository productoRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("Jesus", "jesus@gmail.com"));
                usuarioRepository.save(new Usuario("Joe", "joe@gmail.com"));
            }

            if (productoRepository.count() == 0) {
                productoRepository.save(new Producto("Laptop HP Victus 15", 3500.5));
                productoRepository.save(new Producto("Xiaomi 15", 3150.0));
                productoRepository.save(new Producto("Audífonos Redmi", 180.0));
            }
        };
    }
}
