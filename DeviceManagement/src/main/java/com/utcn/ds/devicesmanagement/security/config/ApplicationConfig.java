package com.utcn.ds.devicesmanagement.security.config;

import com.utcn.ds.devicesmanagement.entity.User;
import com.utcn.ds.devicesmanagement.exception.NotFoundObjectException;
import com.utcn.ds.devicesmanagement.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            String role = determineUserRole(username);
            return switch (role) {
                case "ADMIN" -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new NotFoundObjectException("Admin with email: " + username + " not found!"));
                case "CLIENT" -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new NotFoundObjectException("Client with email " + username + " not found!"));
                default -> throw new IllegalStateException("Invalid role: " + role);
            };
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(userRepository));
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private String determineUserRole(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return user.get().getRole();
        } else {
            throw new NotFoundObjectException("User with email: " + username + " not found!");
        }
    }
}
