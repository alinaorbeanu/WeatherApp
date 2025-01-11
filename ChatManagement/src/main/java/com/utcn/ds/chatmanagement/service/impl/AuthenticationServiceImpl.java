package com.utcn.ds.chatmanagement.service.impl;

import com.utcn.ds.chatmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import com.utcn.ds.chatmanagement.entity.User;
import com.utcn.ds.chatmanagement.exception.EmailAlreadyExistsException;
import com.utcn.ds.chatmanagement.repository.UserRepository;
import com.utcn.ds.chatmanagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public void register(RegisterRequestDTO registerRequestDTO) {
        var userDTO = UserDTO.builder()
                .email(registerRequestDTO.getEmail())
                .name(registerRequestDTO.getName())
                .role(registerRequestDTO.getRole())
                .build();

        var user = mapToUser(userDTO);

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email address already exists: " + registerRequestDTO.getEmail() + " !");
        }
        userRepository.save(user);
    }

    public User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}