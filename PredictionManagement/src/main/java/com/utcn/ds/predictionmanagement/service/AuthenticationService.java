package com.utcn.ds.predictionmanagement.service;


import com.utcn.ds.predictionmanagement.controller.dto.RegisterRequestDTO;

public interface AuthenticationService {

    void register(RegisterRequestDTO registerRequestDTO);
}