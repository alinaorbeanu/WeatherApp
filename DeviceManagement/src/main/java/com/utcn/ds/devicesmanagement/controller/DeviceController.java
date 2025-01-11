package com.utcn.ds.devicesmanagement.controller;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
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

public interface DeviceController {

    /**
     * Stores a new device in the database.
     *
     * @param deviceDTO the device to be inserted
     * @return the stored device
     */
    @PostMapping(value = "/device")
    @Operation(summary = "Add new device", description = "Returns the device created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device was successfully created!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<DeviceDTO> add(@Valid @RequestBody DeviceDTO deviceDTO);

    /**
     * Retrieves a device by id.
     *
     * @param id if of requested device
     * @return requested device
     */
    @GetMapping(value = "/device/{id}")
    @ResponseBody
    @Operation(summary = "Find device by id", description = "Returns the device with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device was found and returned!"),
            @ApiResponse(responseCode = "404", description = "No device was found with that id!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<DeviceDTO> findById(HttpServletRequest request, @Positive @PathVariable Long id);

    /**
     * Retrieves devices.
     *
     * @return a list with all database devices
     */
    @GetMapping(value = "/device")
    @Operation(summary = "Find all devices", description = "Returns devices list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices list returned!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<List<DeviceDTO>> findAll(HttpServletRequest request);

    /**
     * Deletes from the database a device by id.
     *
     * @param id the device's id
     */
    @DeleteMapping(value = "/device/{id}")
    @Operation(description = "Deletes an existing device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device was successfully deleted!"),
            @ApiResponse(responseCode = "404", description = "User not found!"),
            @ApiResponse(responseCode = "403", description = "Forbidden!")
    })
    ResponseEntity<Void> delete(@Positive @PathVariable Long id);

    @PutMapping(value = "/device")
    ResponseEntity<DeviceDTO> updateDevice(@RequestBody(required = false) DeviceDTO deviceDTO);
}
