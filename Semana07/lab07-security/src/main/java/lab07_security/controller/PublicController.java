package lab07_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/api/free")
    public String free() {
        return "Endpoint público disponible";
    }
}