package com.utcn.ds.usermanagement.service.impl;

import com.utcn.ds.usermanagement.controller.dto.RoleDTO;
import com.utcn.ds.usermanagement.controller.dto.UserDTO;
import com.utcn.ds.usermanagement.entity.Role;
import com.utcn.ds.usermanagement.entity.User;
import com.utcn.ds.usermanagement.exception.EmailAlreadyExistsException;
import com.utcn.ds.usermanagement.exception.NotFoundObjectException;
import com.utcn.ds.usermanagement.repository.UserRepository;
import com.utcn.ds.usermanagement.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDTO add(UserDTO userDTO) {
        var userToAdd = UserDTO.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roleDTO(userDTO.getRoleDTO())
                .build();

        var user = mapToUser(userToAdd);

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email address already exists: " + userDTO.getEmail() + " !");
        }

        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::mapToUserDTO).sorted().toList();
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserDTO)
                .orElseThrow(() -> new NotFoundObjectException("User with id: " + id + " not found!"));
    }

    @Override
    public void delete(Long id) {
        Optional<User> userToDelete = userRepository.findById(id);
        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
        } else {
            throw new NotFoundObjectException("User with id: " + id + " not found!");
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserDTO)
                .orElseThrow(() -> new NotFoundObjectException("User with email: " + email + " not found!"));
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> userToUpdate = userRepository.findById(userDTO.getId());
        if (userToUpdate.isEmpty()) {
            throw new NotFoundObjectException("User with id: " + userDTO.getId() + " not found!");
        }

        userToUpdate.get().setName(userDTO.getName());
        userToUpdate.get().setEmail(userDTO.getEmail());
        userToUpdate.get().setRole(this.mapToRole(userDTO.getRoleDTO()));

        return mapToUserDTO(userRepository.save(userToUpdate.get()));
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoleDTO(mapToRoleDTO(user.getRole()));
        return userDTO;
    }

    private Role mapToRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    private RoleDTO mapToRoleDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }
}