package com.tecsup.lab09security.config;

import com.tecsup.lab09security.model.AppUser;
import com.tecsup.lab09security.model.Role;
import com.tecsup.lab09security.repository.RoleRepository;
import com.tecsup.lab09security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner initializeSecurityData(RoleRepository roles, UserRepository users,
                                              PasswordEncoder encoder,
                                              @Value("${app.bootstrap-admin.username}") String username,
                                              @Value("${app.bootstrap-admin.email}") String email,
                                              @Value("${app.bootstrap-admin.password}") String password,
                                              @Value("${app.bootstrap-user.username}") String userName,
                                              @Value("${app.bootstrap-user.email}") String userEmail,
                                              @Value("${app.bootstrap-user.password}") String userPassword,
                                              @Value("${app.bootstrap-moderator.username}") String moderatorName,
                                              @Value("${app.bootstrap-moderator.email}") String moderatorEmail,
                                              @Value("${app.bootstrap-moderator.password}") String moderatorPassword) {
        return args -> initialize(roles, users, encoder,
                new String[][]{
                        {username, email, password, "ADMIN"},
                        {userName, userEmail, userPassword, "USER"},
                        {moderatorName, moderatorEmail, moderatorPassword, "MODERATOR"}
                });
    }

    @Transactional
    void initialize(RoleRepository roles, UserRepository users, PasswordEncoder encoder, String[][] accounts) {
        for (String roleName : List.of("USER", "ADMIN", "MODERATOR")) {
            if (roles.findByName(roleName).isEmpty()) {
                roles.save(new Role(roleName));
            }
        }
        for (String[] account : accounts) {
            String username = account[0];
            String email = account[1];
            String password = account[2];
            String roleName = account[3];
            if (!users.existsByEmailIgnoreCase(email)) {
                Role role = roles.findByName(roleName).orElseThrow();
                AppUser user = new AppUser(username, email, encoder.encode(password));
                user.addRole(role);
                users.save(user);
            }
        }
    }
}
