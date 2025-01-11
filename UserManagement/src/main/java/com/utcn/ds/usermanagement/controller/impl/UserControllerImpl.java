package com.utcn.ds.usermanagement.controller.impl;

import com.utcn.ds.usermanagement.controller.UserController;
import com.utcn.ds.usermanagement.controller.dto.UserDTO;
import com.utcn.ds.usermanagement.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserDTO> add(UserDTO userDTO) {
        return new ResponseEntity<>(userService.add(userDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDTO> findById(Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        userService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> findByEmail(String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findConnection() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
