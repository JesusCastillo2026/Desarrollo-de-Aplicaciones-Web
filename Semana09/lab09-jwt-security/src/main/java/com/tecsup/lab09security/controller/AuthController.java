package com.tecsup.lab09security.controller;

import com.tecsup.lab09security.dto.*;
import com.tecsup.lab09security.jwt.JwtUtils;
import com.tecsup.lab09security.model.AppUser;
import com.tecsup.lab09security.model.Role;
import com.tecsup.lab09security.repository.RoleRepository;
import com.tecsup.lab09security.repository.UserRepository;
import com.tecsup.lab09security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody SignupRequest request) {
        if (request.roles() != null && request.roles().stream()
                .anyMatch(role -> !"USER".equalsIgnoreCase(role))) {
            return ResponseEntity.badRequest().body(new MessageResponse(
                    "El registro público solo permite el rol USER."));
        }
        if (users.existsByUsernameIgnoreCase(request.username().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("El nombre de usuario ya está registrado."));
        }
        if (users.existsByEmailIgnoreCase(request.email().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("El correo ya está registrado."));
        }
        Role userRole = roles.findByName("USER").orElseThrow();
        AppUser user = new AppUser(request.username().trim(), request.email().trim().toLowerCase(),
                passwordEncoder.encode(request.password()));
        user.addRole(userRole);
        users.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Usuario registrado correctamente con rol USER."));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            List<String> authorities = principal.getAuthorities().stream()
                    .map(authority -> authority.getAuthority()).toList();
            return ResponseEntity.ok(new JwtResponse(jwtUtils.generateJwtToken(authentication), "Bearer",
                    principal.getId(), principal.getDisplayName(), principal.getEmail(), authorities));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Correo o contraseña incorrectos."));
        }
    }
}
