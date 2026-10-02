package lab07_security.config;

import lab07_security.model.Role;
import lab07_security.model.User;
import lab07_security.repository.RoleRepository;
import lab07_security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            Role userRole = getOrCreateRole(roleRepository, "ROLE_USER");
            Role adminRole = getOrCreateRole(roleRepository, "ROLE_ADMIN");
            Role managerRole = getOrCreateRole(roleRepository, "ROLE_MANAGER");

            createUserIfAbsent(
                    userRepository, passwordEncoder,
                    "user", "User@S07", userRole
            );

            createUserIfAbsent(
                    userRepository, passwordEncoder,
                    "admin", "Admin@S07", adminRole
            );

            createUserIfAbsent(
                    userRepository, passwordEncoder,
                    "manager", "Manager@S07", managerRole
            );
        };
    }

    private Role getOrCreateRole(RoleRepository repository, String roleName) {
        return repository.findByName(roleName)
                .orElseGet(() -> repository.save(new Role(roleName)));
    }

    private void createUserIfAbsent(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String rawPassword,
            Role role) {

        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(username, passwordEncoder.encode(rawPassword));
            user.addRole(role);
            userRepository.save(user);
        }
    }
}