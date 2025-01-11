package com.utcn.ds.usermanagement.controller;


import com.utcn.ds.usermanagement.controller.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface UserController {

    /**
     * Stores a new user in the database.
     *
     * @param userDTO the user to be inserted
     * @return the stored user
     */
    @PostMapping(value = "/user")
    @Operation(summary = "Add new user", description = "Returns the user created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was successfully created!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<UserDTO> add(@Valid @RequestBody UserDTO userDTO);

    /**
     * Retrieves a user by id.
     *
     * @param id if of requested user
     * @return requested user
     */
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    @Operation(summary = "Find user by id", description = "Returns the user with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found and returned!"),
            @ApiResponse(responseCode = "404", description = "No user was found with that id!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<UserDTO> findById(@Positive @PathVariable Long id);

    /**
     * Retrieves users.
     *
     * @return a list with all database users
     */
    @GetMapping(value = "/user")
    @Operation(summary = "Find all users", description = "Returns users list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list returned!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<List<UserDTO>> findAll();

    /**
     * Deletes from the database a user by id.
     *
     * @param id the user's id
     */
    @DeleteMapping(value = "/user/{id}")
    @Operation(description = "Deletes an existing user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was successfully deleted!"),
            @ApiResponse(responseCode = "404", description = "User not found!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<?> delete(@Positive @PathVariable Long id);

    /**
     * Retrieves a user by email.
     *
     * @param email email of requested user
     * @return requested user
     */
    @GetMapping(value = "/user/email/{email}")
    @ResponseBody
    @Operation(summary = "Find user by email", description = "Returns the user with the given email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found and returned!"),
            @ApiResponse(responseCode = "404", description = "No user was found with that email!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<UserDTO> findByEmail(@Positive @PathVariable String email);

    @PutMapping(value = "/user")
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO);

    @GetMapping(value = "/verifyConnection")
    ResponseEntity<?> findConnection();


}
