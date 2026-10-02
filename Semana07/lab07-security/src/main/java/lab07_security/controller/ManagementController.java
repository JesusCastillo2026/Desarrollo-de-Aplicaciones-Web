package lab07_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagementController {

    @GetMapping("/management/dashboard")
    public String dashboard() {
        return "Panel de administración";
    }
}