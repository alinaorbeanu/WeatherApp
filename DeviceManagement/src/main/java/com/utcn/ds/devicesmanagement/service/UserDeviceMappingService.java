package com.utcn.ds.devicesmanagement.service;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserDeviceMappingService {

    List<DeviceDTO> findAllUserDevices(HttpServletRequest request, String email);
}
