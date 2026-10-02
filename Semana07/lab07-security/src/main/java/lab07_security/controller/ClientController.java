package lab07_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @GetMapping("/client/home")
    public String home() {
        return "Bienvenido al área de clientes";
    }
}