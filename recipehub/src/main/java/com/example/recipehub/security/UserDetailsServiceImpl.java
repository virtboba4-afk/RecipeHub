package com.example.recipehub.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.repositories.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Загрузка пользователя по email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email)
        );
        return new CustomUserDetails(user);
    }
}
