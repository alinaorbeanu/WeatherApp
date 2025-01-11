package com.utcn.ds.devicesmanagement.controller.impl;

import com.utcn.ds.devicesmanagement.controller.AuthenticationController;
import com.utcn.ds.devicesmanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.devicesmanagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO) {
        return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authenticationService.login(loginRequestDTO), HttpStatus.OK);
    }
}