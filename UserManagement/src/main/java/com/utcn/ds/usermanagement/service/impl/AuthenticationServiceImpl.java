package com.utcn.ds.usermanagement.service.impl;

import com.utcn.ds.usermanagement.controller.auth.response.AuthenticationResponse;
import com.utcn.ds.usermanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.usermanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.usermanagement.controller.dto.UserDTO;
import com.utcn.ds.usermanagement.entity.User;
import com.utcn.ds.usermanagement.exception.EmailAlreadyExistsException;
import com.utcn.ds.usermanagement.exception.NotFoundObjectException;
import com.utcn.ds.usermanagement.repository.UserRepository;
import com.utcn.ds.usermanagement.service.AuthenticationService;
import com.utcn.ds.usermanagement.service.security.JwtService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final ModelMapper modelMapper;

    @Override
    public AuthenticationResponse register(RegisterRequestDTO registerRequestDTO) {
        var userDTO = UserDTO.builder()
                .name(registerRequestDTO.getName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .roleDTO(registerRequestDTO.getRoleDTO())
                .build();

        var user = mapToUser(userDTO);

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email address already exists: " + registerRequestDTO.getEmail() + " !");
        }

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        Optional<User> user = userRepository.findByEmail(email);

        String jwtToken = user.map(jwtService::generateToken)
                .orElseThrow(() -> new NotFoundObjectException("User with email: " + email + " not found!"));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}