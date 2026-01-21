package com.example.recipehub.services;

import com.example.recipehub.dto.LoginDto;
import com.example.recipehub.dto.RegisterDto;
import com.example.recipehub.models.entities.Role;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.models.enums.UserRoles;
import com.example.recipehub.repositories.RoleRepository;
import com.example.recipehub.repositories.UserRepository;
import com.example.recipehub.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           SecurityContextRepository securityContextRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityContextRepository = securityContextRepository;
        log.info("AuthServiceImpl инициализирован");
    }

    @Override
    @Transactional
    public void register(RegisterDto dto) {
        log.debug("Регистрация нового пользователя: email={}", dto.getEmail());

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            log.warn("Пароли не совпадают при регистрации: email={}", dto.getEmail());
            throw new RuntimeException("Пароли не совпадают!");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Попытка регистрации с уже существующим email={}", dto.getEmail());
            throw new RuntimeException("Email уже используется!");
        }

        Role userRole = roleRepository.findByRole(UserRoles.USER);
        if (userRole == null) {
            throw new RuntimeException("Роль USER не найдена в БД!");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singletonList(userRole));

        userRepository.save(user);

        log.info("Пользователь успешно зарегистрирован: id={}, email={}",
                user.getId(), user.getEmail());
    }

    @Override
    @Transactional
    public boolean login(LoginDto dto,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        log.debug("Попытка входа: email={}", dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Пользователь с email={} не найден", dto.getEmail());
                    return new UsernameNotFoundException("Пользователь не найден");
                });

        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPasswordHash());
        if (!matches) {
            log.warn("Неверный пароль для email={}", dto.getEmail());
            return false;
        }


        CustomUserDetails userDetails = new CustomUserDetails(user);


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);


        securityContextRepository.saveContext(context, request, response);

        log.info("Пользователь успешно вошёл: id={}, email={}", user.getId(), user.getEmail());
        return true;
    }

    public User getUser(String email) {
        log.debug("Загрузка пользователя по email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с email " + email + " не найден!"
                ));
    }
}
