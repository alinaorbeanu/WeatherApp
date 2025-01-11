package com.utcn.ds.devicesmanagement.service;

import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    /**
     * Stores a new user in the database.
     *
     * @param userDTO the user to be inserted
     * @return the stored user
     */
    UserDTO add(HttpServletRequest request, UserDTO userDTO);

    /**
     * Retrieves users.
     *
     * @return a list with all database users
     */
    List<UserDTO> findAll(String token);

    /**
     * Retrieves a user by id.
     *
     * @param id if of requested user
     * @return requested user
     */
    UserDTO findById(HttpServletRequest request, Long id);

    /**
     * Deletes from the database a user by id.
     *
     * @param id the user's id
     */
    void delete(HttpServletRequest request, Long id);

    /**
     * Retrieves a user by email.
     *
     * @param email email of requested user
     * @return requested user
     */
    UserDTO findByEmail(HttpServletRequest request, String email);

    UserDTO updateUser(String token, UserDTO userDTO);
}
