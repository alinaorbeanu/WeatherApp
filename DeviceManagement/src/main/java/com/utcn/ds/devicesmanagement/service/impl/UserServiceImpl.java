package com.utcn.ds.devicesmanagement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utcn.ds.devicesmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RoleDTO;
import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import com.utcn.ds.devicesmanagement.entity.User;
import com.utcn.ds.devicesmanagement.repository.UserRepository;
import com.utcn.ds.devicesmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDTO add(HttpServletRequest request, UserDTO userDTO) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(1000));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMono = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .build())
                .body(BodyInserters.fromValue(userDTO))
                .retrieve()
                .bodyToMono(String.class);

        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .role(userDTO.getRoleDTO().getName())
                .build();

        WebClient clientChat = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://chat:8086")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
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
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
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
            String chat = responseMonoChat.block();
            String pred = responseMonoPrediction.block();
            userDTO = new ObjectMapper().readValue(s, UserDTO.class);
            User user = mapToUser(userDTO);
            this.userRepository.save(user);
            return userDTO;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            User user = mapToUser(userDTO);
            return mapToUserDTO(userRepository.save(user));
        }
    }

    @Override
    public List<UserDTO> findAll(String token) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            return new ObjectMapper().readValue(s, new TypeReference<List<UserDTO>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            List<User> users = this.userRepository.findAll();
            return users.stream().map(this::mapToUserDTO).sorted().toList();
        }
    }

    @Override
    public UserDTO findById(HttpServletRequest request, Long id) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            return new ObjectMapper().readValue(s, UserDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            User user = this.userRepository.findById(id).get();
            return mapToUserDTO(user);
        }
    }

    @Override
    public void delete(HttpServletRequest request, Long id) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMono = client.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        WebClient clientChat = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://chat:8086")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMonoChat = clientChat.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        WebClient clientPred = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://prediction:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMonoPred = clientPred.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        try {
            responseMono.block();
            responseMonoChat.block();
            responseMonoPred.block();
            this.userRepository.deleteById(id);
        } catch (Exception exception) {
            this.userRepository.deleteById(id);
        }
    }

    @Override
    public UserDTO findByEmail(HttpServletRequest request, String email) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"))
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/email/{email}")
                        .build(email))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            return new ObjectMapper().readValue(s, UserDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            User user = this.userRepository.findByEmail(email).get();
            return mapToUserDTO(user);
        }
    }

    @Override
    public UserDTO updateUser(String token, UserDTO userDTO) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        Mono<String> responseMono = client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .build())
                .body(BodyInserters.fromValue(userDTO))
                .retrieve()
                .bodyToMono(String.class);


        WebClient clientChat = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://chat:8086")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        Mono<String> responseMonoChat = clientChat.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .build())
                .body(BodyInserters.fromValue(userDTO))
                .retrieve()
                .bodyToMono(String.class);

        WebClient clientPred = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://prediction:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        Mono<String> responseMonoPred = clientPred.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .build())
                .body(BodyInserters.fromValue(userDTO))
                .retrieve()
                .bodyToMono(String.class);

        try {
            String s = responseMono.block();
            String sChat = responseMonoChat.block();
            String sPred = responseMonoPred.block();
            User user = this.userRepository.findById(userDTO.getId()).get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRoleDTO().getName());
            userRepository.save(user);
            return new ObjectMapper().readValue(s, UserDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception exception) {
            User user = this.userRepository.findById(userDTO.getId()).get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRoleDTO().getName());
            return mapToUserDTO(userRepository.save(user));
        }
    }

    private User mapToUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setRole(userDTO.getRoleDTO().getName());
        return user;
    }

    private UserDTO mapToUserDTO(User user) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(user.getRole());
        switch (user.getRole()) {
            case ("ADMIN") -> roleDTO.setId(1L);
            case ("CLIENT") -> roleDTO.setId(2L);
        }
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .roleDTO(roleDTO)
                .build();
    }
}