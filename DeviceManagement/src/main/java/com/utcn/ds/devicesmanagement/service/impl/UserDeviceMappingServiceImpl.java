package com.utcn.ds.devicesmanagement.service.impl;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import com.utcn.ds.devicesmanagement.entity.UserDeviceMapping;
import com.utcn.ds.devicesmanagement.repository.UserDeviceMappingRepository;
import com.utcn.ds.devicesmanagement.service.DeviceService;
import com.utcn.ds.devicesmanagement.service.UserDeviceMappingService;
import com.utcn.ds.devicesmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceMappingServiceImpl implements UserDeviceMappingService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDeviceMappingRepository userDeviceMappingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DeviceDTO> findAllUserDevices( HttpServletRequest request, String email) {

        List<DeviceDTO> userDevices = new ArrayList<>();

        Optional<UserDTO> userDTO = Optional.ofNullable(this.userService.findByEmail(request, email));

        if (userDTO.isPresent()) {
            List<UserDeviceMapping> userDevicesIds = this.userDeviceMappingRepository.getAllByUserId(userDTO.get().getId());

            for (UserDeviceMapping entry : userDevicesIds) {
                userDevices.add(modelMapper.map(entry.getDevice(), DeviceDTO.class));
            }
        }
        return userDevices;
    }
}
