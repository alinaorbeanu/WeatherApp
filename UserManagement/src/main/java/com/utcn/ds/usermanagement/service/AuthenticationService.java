package com.utcn.ds.usermanagement.service;

import com.utcn.ds.usermanagement.controller.auth.response.AuthenticationResponse;
import com.utcn.ds.usermanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.usermanagement.controller.dto.RegisterRequestDTO;

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