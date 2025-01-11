package com.utcn.ds.devicesmanagement.service;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface DeviceService {

    /**
     * Stores a new device in the database.
     *
     * @param deviceDTO the device to be inserted
     * @return the stored device
     */
    DeviceDTO add(DeviceDTO deviceDTO);

    /**
     * Retrieves devices.
     *
     * @return a list with all database devices
     */
    List<DeviceDTO> findAll(HttpServletRequest request);

    /**
     * Retrieves a device by id.
     *
     * @param id if of requested device
     * @return requested device
     */
    DeviceDTO findById(HttpServletRequest request, Long id);

    /**
     * Deletes from the database a device by id.
     *
     * @param id the device's id
     */
    void delete(Long id);

    DeviceDTO updateDevice(DeviceDTO deviceDTO);
}
