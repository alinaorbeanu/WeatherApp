package com.utcn.ds.devicesmanagement.controller;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

public interface UserDeviceMappingController {

    @GetMapping(value = "/user/device/{email}")
    @ResponseBody
    @Operation(summary = "Find user's devices", description = "Returns the user's devices with the given email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's devices were found and returned!"),
            @ApiResponse(responseCode = "404", description = "No user's devices were found with that email!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<List<DeviceDTO>> findAllUserDevices(HttpServletRequest request, @Positive @PathVariable String email);
}
