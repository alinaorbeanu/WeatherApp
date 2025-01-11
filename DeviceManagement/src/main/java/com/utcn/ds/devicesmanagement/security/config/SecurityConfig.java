package com.utcn.ds.devicesmanagement.security.config;

import com.utcn.ds.devicesmanagement.security.JwtAuthenticationFilter;
import io.swagger.models.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/auth/login", "/auth/register").permitAll()
                                .requestMatchers("/api/websocket/**").permitAll()
                                .requestMatchers("/verifyConnection").permitAll()
                                .requestMatchers("/user/email/**").hasAnyAuthority("CLIENT", "ADMIN")
                                .requestMatchers("/user/device/**").hasAuthority("CLIENT")
                                .requestMatchers("/user/{id}").hasAnyAuthority("CLIENT", "ADMIN")
                                .requestMatchers("/user/**").hasAuthority("ADMIN")
                                .requestMatchers("/sensor-data/**").hasAuthority("CLIENT")
                                .requestMatchers("/device/**").hasAuthority("ADMIN")
                                .requestMatchers(String.valueOf(HttpMethod.GET), "/device/**").hasAnyAuthority("CLIENT", "ADMIN")
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }
}
