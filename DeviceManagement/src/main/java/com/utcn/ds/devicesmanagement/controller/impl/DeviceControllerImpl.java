package com.utcn.ds.devicesmanagement.controller.impl;

import com.utcn.ds.devicesmanagement.controller.DeviceController;
import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import com.utcn.ds.devicesmanagement.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class DeviceControllerImpl implements DeviceController {
    private final DeviceService deviceService;

    @Override
    public ResponseEntity<DeviceDTO> add(DeviceDTO deviceDTO) {
        return new ResponseEntity<>(deviceService.add(deviceDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DeviceDTO> findById(HttpServletRequest request, Long id) {
        return new ResponseEntity<>(deviceService.findById(request, id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DeviceDTO>> findAll(HttpServletRequest request) {
        return new ResponseEntity<>(deviceService.findAll(request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        deviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<DeviceDTO> updateDevice(DeviceDTO deviceDTO) {
        return new ResponseEntity<>(deviceService.updateDevice(deviceDTO), HttpStatus.OK);
    }
}
