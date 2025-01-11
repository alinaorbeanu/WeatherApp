package com.utcn.ds.predictionmanagement.controller;

import com.utcn.ds.predictionmanagement.controller.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
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
    ResponseEntity<UserDTO> add(HttpServletRequest request, @RequestBody UserDTO userDTO);

    /**
     * Retrieves a user by id.
     *
     * @param id if of requested user
     * @return requested user
     */
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    ResponseEntity<UserDTO> findById(HttpServletRequest request, @PathVariable Long id);

    /**
     * Retrieves users.
     *
     * @return a list with all database users
     */
    @GetMapping(value = "/user")
    ResponseEntity<List<UserDTO>> findAll(HttpServletRequest request);

    /**
     * Deletes from the database a user by id.
     *
     * @param id the user's id
     */
    @DeleteMapping(value = "/user/{id}")
    ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long id);

    /**
     * Retrieves a user by email.
     *
     * @param email email of requested user
     * @return requested user
     */
    @GetMapping(value = "/user/email/{email}")
    @ResponseBody
    ResponseEntity<UserDTO> findByEmail(HttpServletRequest request, @PathVariable String email);

    @PutMapping(value = "/user")
    ResponseEntity<UserDTO> updateUser(HttpServletRequest request, @RequestBody(required = false) UserDTO userDTO);
}

