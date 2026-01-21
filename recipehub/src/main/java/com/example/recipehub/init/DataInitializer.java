package com.example.recipehub.init;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.recipehub.models.entities.Role;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.models.enums.UserRoles;
import com.example.recipehub.repositories.RoleRepository;
import com.example.recipehub.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.moderator.email}")
    private String moderatorEmail;

    @Value("${app.moderator.password}")
    private String moderatorPassword;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("=== Инициализация данных RecipeHub ===");

        // роли
        Role userRole = ensureRole(UserRoles.USER);
        Role moderatorRole = ensureRole(UserRoles.MODERATOR);
        Role adminRole = ensureRole(UserRoles.ADMIN);

        // админ
        ensureAdminUser(adminEmail, adminPassword, adminRole);

        // модератор
        ensureModeratorUser(moderatorEmail, moderatorPassword, moderatorRole);

        log.info("=== Инициализация данных завершена ===");
    }

    private Role ensureRole(UserRoles roleEnum) {
        Role role = roleRepository.findByRole(roleEnum);
        if (role == null) {
            log.info("Роль {} не найдена, создаём", roleEnum);
            role = new Role(roleEnum);
            role = roleRepository.save(role);
        }
        return role;
    }

    private void ensureAdminUser(String email, String rawPassword, Role adminRole) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            log.info("Админ с email {} уже существует", email);
            return;
        }

        log.info("Создаём администратора с email {}", email);
        User admin = new User();
        admin.setFullName("Admin");
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(rawPassword));
        admin.setRoles(Collections.singletonList(adminRole));

        userRepository.save(admin);
    }

    private void ensureModeratorUser(String email, String rawPassword, Role moderatorRole) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            log.info("Модератор с email {} уже существует", email);
            return;
        }

        log.info("Создаём модератора с email {}", email);
        User moderator = new User();
        moderator.setFullName("Moderator");
        moderator.setEmail(email);
        moderator.setPasswordHash(passwordEncoder.encode(rawPassword));
        moderator.setRoles(Collections.singletonList(moderatorRole));

        userRepository.save(moderator);
    }
}

