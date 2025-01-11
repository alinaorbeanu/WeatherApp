package com.utcn.ds.usermanagement.controller.auth.impl;

import com.utcn.ds.usermanagement.controller.auth.AuthenticationController;
import com.utcn.ds.usermanagement.controller.auth.response.AuthenticationResponse;
import com.utcn.ds.usermanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.usermanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.usermanagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthenticationResponse> register(RegisterRequestDTO registerRequestDTO) {
        return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authenticationService.login(loginRequestDTO), HttpStatus.OK);
    }
}