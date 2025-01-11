package com.utcn.ds.devicesmanagement.scheduler;

import com.utcn.ds.devicesmanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RoleDTO;
import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import com.utcn.ds.devicesmanagement.controller.response.AuthenticationResponse;
import com.utcn.ds.devicesmanagement.entity.User;
import com.utcn.ds.devicesmanagement.repository.UserRepository;
import com.utcn.ds.devicesmanagement.service.AuthenticationService;
import com.utcn.ds.devicesmanagement.service.UserService;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class SchedulerConfig {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Scheduled(fixedRate = 1000 * 5)
    public void scheduleTask() {
        String strDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://user:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> responseMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/verifyConnection")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        try {
            responseMono.block();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            strDate = dateFormat.format(new Date());
            System.out.println("Databases synchronization failed at - " + strDate);
            return;
        }

        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .email("admin")
                .password("admin")
                .build();
        AuthenticationResponse authenticationResponse = this.authenticationService.login(loginRequestDTO);

        List<UserDTO> usersManagement = this.userService.findAll("Bearer " + authenticationResponse.getToken());
        List<User> userList = this.userRepository.findAll();
        List<UserDTO> userDeviceManagement = userList.stream().map(this::mapToUserDTO).sorted().toList();
        List<Long> idsList = usersManagement.stream().map(UserDTO::getId).sorted().toList();

        for (UserDTO userDTO : userDeviceManagement) {
            if (!idsList.contains(userDTO.getId())) {
                RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                        .name(userDTO.getName())
                        .email(userDTO.getEmail())
                        .password(userDTO.getPassword())
                        .roleDTO(userDTO.getRoleDTO()).build();
                this.authenticationService.register(registerRequestDTO);
            } else {
                this.userService.updateUser("Bearer " + authenticationResponse.getToken(), userDTO);
            }
        }

        strDate = dateFormat.format(new Date());

        System.out.println("Databases synchronization was done with successfully at - " + strDate);
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(user.getRole());
        switch (user.getRole()) {
            case ("ADMIN") -> roleDTO.setId(1L);
            case ("CLIENT") -> roleDTO.setId(2L);
            default -> {
            }
        }
        userDTO.setRoleDTO(roleDTO);
        userDTO.setRole(roleDTO.getName());
        return userDTO;
    }
}
