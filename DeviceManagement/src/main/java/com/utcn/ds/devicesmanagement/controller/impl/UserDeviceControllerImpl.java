package com.utcn.ds.devicesmanagement.controller.impl;

import com.utcn.ds.devicesmanagement.controller.UserDeviceMappingController;
import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import com.utcn.ds.devicesmanagement.service.UserDeviceMappingService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserDeviceControllerImpl implements UserDeviceMappingController {

    @Autowired
    private UserDeviceMappingService userDeviceMappingService;

    @Override
    public ResponseEntity<List<DeviceDTO>> findAllUserDevices(HttpServletRequest request, String email) {
        return new ResponseEntity<>(userDeviceMappingService.findAllUserDevices(request, email), HttpStatus.OK);
    }
}
