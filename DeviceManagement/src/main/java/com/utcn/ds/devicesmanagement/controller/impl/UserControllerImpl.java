package com.utcn.ds.devicesmanagement.controller.impl;

import com.utcn.ds.devicesmanagement.controller.UserController;
import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import com.utcn.ds.devicesmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserDTO> add(HttpServletRequest request, UserDTO userDTO) {
        return new ResponseEntity<>(userService.add(request, userDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDTO> findById(HttpServletRequest request, Long id) {
        return new ResponseEntity<>(userService.findById(request, id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAll(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findAll(request.getHeader("Authorization")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(HttpServletRequest request, Long id) {
        userService.delete(request, id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> findByEmail(HttpServletRequest request, String email) {
        return new ResponseEntity<>(userService.findByEmail(request, email), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(HttpServletRequest request, UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(request.getHeader("Authorization"), userDTO), HttpStatus.OK);
    }
}

