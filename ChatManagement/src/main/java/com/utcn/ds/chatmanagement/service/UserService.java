package com.utcn.ds.chatmanagement.service;


import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import java.util.List;

public interface UserService {

    /**
     * Stores a new user in the database.
     *
     * @param userDTO the user to be inserted
     * @return the stored user
     */
    UserDTO add(UserDTO userDTO);

    /**
     * Retrieves users.
     *
     * @return a list with all database users
     */
    List<UserDTO> findAll();

    /**
     * Retrieves a user by id.
     *
     * @param id if of requested user
     * @return requested user
     */
    UserDTO findById(Long id);

    /**
     * Deletes from the database a user by id.
     *
     * @param id the user's id
     */
    void delete(Long id);

    /**
     * Retrieves a user by email.
     *
     * @param email email of requested user
     * @return requested user
     */
    UserDTO findByEmail(String email);

    UserDTO updateUser(UserDTO userDTO);
}