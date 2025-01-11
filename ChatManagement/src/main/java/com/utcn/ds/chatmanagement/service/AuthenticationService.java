package com.utcn.ds.chatmanagement.service;


import com.utcn.ds.chatmanagement.controller.dto.RegisterRequestDTO;

public interface AuthenticationService {

    void register(RegisterRequestDTO registerRequestDTO);
}