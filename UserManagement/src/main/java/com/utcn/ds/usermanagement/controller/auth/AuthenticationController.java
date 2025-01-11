package com.utcn.ds.usermanagement.controller.auth;

import com.utcn.ds.usermanagement.controller.auth.response.AuthenticationResponse;
import com.utcn.ds.usermanagement.controller.dto.LoginRequestDTO;
import com.utcn.ds.usermanagement.controller.dto.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {
    /**
     * Registers a new client into database.
     *
     * @param registerRequestDTO contains the client's details
     * @return a token for registered client
     */
    @PostMapping(value = "/auth/register")
    @Operation(summary = "Registers a new client into database", description = "Returns a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client is added and token is retrieved!"),
            @ApiResponse(responseCode = "400", description = "Bad request!")
    })
    ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO);

    /**
     * Authenticates a user or a client.
     *
     * @param loginRequestDTO contains the user's or client's credentials
     * @return a token for authenticated user or client
     */
    @PostMapping(value = "/auth/login")
    @Operation(summary = "Authenticates a user or a client", description = "Returns a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User or client is authenticated and token is retrieved!"),
            @ApiResponse(responseCode = "500", description = "Bad Request!")
    })
    ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequestDTO loginRequestDTO);
}
