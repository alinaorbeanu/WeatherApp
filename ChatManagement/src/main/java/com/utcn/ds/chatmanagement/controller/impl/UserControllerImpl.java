package com.utcn.ds.chatmanagement.controller.impl;

import com.utcn.ds.chatmanagement.controller.UserController;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import com.utcn.ds.chatmanagement.service.UserService;
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
        return new ResponseEntity<>(userService.add(userDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDTO> findById(HttpServletRequest request, Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAll(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(HttpServletRequest request, Long id) {
        userService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> findByEmail(HttpServletRequest request, String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(HttpServletRequest request, UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }
}

