package com.utcn.ds.devicesmanagement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcn.ds.devicesmanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.devicesmanagement.controller.response.AuthenticationResponse;
import com.utcn.ds.devicesmanagement.entity.User;
import com.utcn.ds.devicesmanagement.exception.AlreadyExistsException;
import com.utcn.ds.devicesmanagement.exception.NotFoundObjectException;
import com.utcn.ds.devicesmanagement.repository.UserRepository;
import com.utcn.ds.devicesmanagement.service.AuthenticationService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(RegisterRequestDTO registerRequestDTO) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        registerRequestDTO.setRole(registerRequestDTO.getRoleDTO().getName());

        //save on user project
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMono = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/register")
                        .build())
                .body(BodyInserters.fromValue(registerRequestDTO))
                .retrieve()
                .bodyToMono(String.class);

        //save on chat project

        WebClient clientChat = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://chat:8086")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMonoChat = clientChat.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/register")
                        .build())
                .body(BodyInserters.fromValue(registerRequestDTO))
                .retrieve()
                .bodyToMono(String.class);

        WebClient clientPrediction = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://prediction:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMonoPrediction = clientPrediction.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/register")
                        .build())
                .body(BodyInserters.fromValue(registerRequestDTO))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            String sPrediction = responseMonoPrediction.block();
            String sChat = responseMonoChat.block();
            User user = User.builder()
                    .name(registerRequestDTO.getName())
                    .email(registerRequestDTO.getEmail())
                    .password(registerRequestDTO.getPassword())
                    .role(registerRequestDTO.getRoleDTO().getName())
                    .build();
            if (this.userRepository.findByEmail(user.getEmail()).isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                this.userRepository.save(user);
            } else {
                throw new AlreadyExistsException("This email already exists");
            }
            authenticationResponse = new ObjectMapper().readValue(s, AuthenticationResponse.class);
            return new ObjectMapper().readValue(s, AuthenticationResponse.class);
        } catch (JsonProcessingException e) {
            return authenticationResponse;
        }
    }

    @Override
    public AuthenticationResponse login(LoginRequestDTO loginRequestDTO) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMono = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/login")
                        .build())
                .body(BodyInserters.fromValue(loginRequestDTO))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            return new ObjectMapper().readValue(s, AuthenticationResponse.class);
        } catch (WebClientRequestException | JsonProcessingException e) {
            throw new NotFoundObjectException("Username and password are wrong!");
        }
    }
}