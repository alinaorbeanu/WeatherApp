package com.utcn.ds.predictionmanagement.controller.auth;

import com.utcn.ds.predictionmanagement.controller.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {

    @PostMapping(value = "/auth/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO);
}