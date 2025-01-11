package com.utcn.ds.devicesmanagement.service;

import com.utcn.ds.devicesmanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.devicesmanagement.controller.dto.RegisterRequestDTO;
import com.utcn.ds.devicesmanagement.controller.response.AuthenticationResponse;

public interface AuthenticationService {
    /**
     * Registers a new client with the provided registration details and returns an authentication response containing a token.
     *
     * @param registerRequestDTO the registration details for the new client
     * @return an authentication response containing the token for the new client
     */
    AuthenticationResponse register(RegisterRequestDTO registerRequestDTO);

    /**
     * Authenticates a user or a client with the provided login details and returns an authentication response containing a token.
     *
     * @param loginRequestDTO the login details for user or client
     * @return an authentication response containing a token for the authenticated user or client
     */
    AuthenticationResponse login(LoginRequestDTO loginRequestDTO);
}
