package com.utcn.ds.predictionmanagement.controller.auth.impl;

import com.utcn.ds.predictionmanagement.controller.auth.AuthenticationController;
import com.utcn.ds.predictionmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.predictionmanagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO) {
        authenticationService.register(registerRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}