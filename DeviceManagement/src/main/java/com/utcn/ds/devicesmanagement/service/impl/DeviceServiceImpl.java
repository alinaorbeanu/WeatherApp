package com.utcn.ds.devicesmanagement.service.impl;

import com.utcn.ds.devicesmanagement.controller.dto.DeviceDTO;
import com.utcn.ds.devicesmanagement.controller.dto.UserDTO;
import com.utcn.ds.devicesmanagement.entity.Device;
import com.utcn.ds.devicesmanagement.entity.User;
import com.utcn.ds.devicesmanagement.entity.UserDeviceMapping;
import com.utcn.ds.devicesmanagement.exception.NotFoundObjectException;
import com.utcn.ds.devicesmanagement.repository.DeviceRepository;
import com.utcn.ds.devicesmanagement.repository.UserDeviceMappingRepository;
import com.utcn.ds.devicesmanagement.repository.UserRepository;
import com.utcn.ds.devicesmanagement.service.DeviceService;
import com.utcn.ds.devicesmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final ModelMapper modelMapper;

    private final DeviceRepository deviceRepository;

    private final UserService userService;

    private final UserDeviceMappingRepository userDeviceMappingRepository;
    private final UserRepository userRepository;

    @Override
    public DeviceDTO add(DeviceDTO deviceDTO) {
        Device device = mapToDevice(deviceDTO);

        return mapToDeviceDTO(deviceRepository.save(device));
    }

    @Override
    public List<DeviceDTO> findAll(HttpServletRequest request) {
        List<Device> devices = deviceRepository.findAll();

        return devices.stream().map(x -> mapToDisplayDeviceDTO(request, x)).sorted().toList();
    }

    @Override
    public DeviceDTO findById(HttpServletRequest request, Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundObjectException("Device not found"));
        return mapToDisplayDeviceDTO(request, device);
    }

    @Override
    public void delete(Long id) {
        Optional<Device> deviceToDeleted = deviceRepository.findById(id);
        if (deviceToDeleted.isPresent()) {
            deviceRepository.delete(deviceToDeleted.get());
        } else {
            throw new NotFoundObjectException("Device with id: " + id + " not found!");
        }
    }

    private Device mapToDevice(DeviceDTO deviceDTO) {
        return modelMapper.map(deviceDTO, Device.class);
    }

    private User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private DeviceDTO mapToDisplayDeviceDTO(HttpServletRequest request, Device device) {
        DeviceDTO deviceDTO = modelMapper.map(device, DeviceDTO.class);
        if (device.getUserDeviceMappings().isEmpty()) {
            deviceDTO.setOwnerId("0");
        } else {
            UserDTO userDTO = this.userService.findById(request, device.getUserDeviceMappings().get(0).getUserId());
            deviceDTO.setOwner(userDTO.getEmail());
            deviceDTO.setOwnerId(device.getUserDeviceMappings().get(0).getUserId().toString());
        }

        return deviceDTO;
    }

    private DeviceDTO mapToDeviceDTO(Device device) {
        return modelMapper.map(device, DeviceDTO.class);
    }

    @Override
    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        Optional<Device> deviceToUpdate = deviceRepository.findById(deviceDTO.getId());
        if (deviceToUpdate.isEmpty()) {
            throw new NotFoundObjectException("Device with id: " + deviceDTO.getId() + " not found!");
        }

        Optional<UserDeviceMapping> mapping = this.userDeviceMappingRepository.findByDeviceId(deviceDTO.getId());
        if (mapping.isPresent()) {
            if (deviceDTO.getOwnerId().equals("0")) {
                this.userDeviceMappingRepository.deleteByUserIdAndDeviceId(mapping.get().getUserId(), deviceToUpdate.get().getId());
                return null;
            } else if (!deviceDTO.getOwnerId().equals(mapping.get().getUserId().toString())) {
                this.userDeviceMappingRepository.deleteByUserIdAndDeviceId(mapping.get().getUserId(), deviceToUpdate.get().getId());

                Optional<User> userToUpdate = userRepository.findById(Long.parseLong(deviceDTO.getOwnerId()));
                if (userToUpdate.isEmpty()) {
                    throw new NotFoundObjectException("User with id: " + deviceDTO.getOwnerId() + " not found!");
                }

                UserDeviceMapping newMapping = UserDeviceMapping.builder().user(userToUpdate.get()).device(deviceToUpdate.get())
                        .deviceId(deviceToUpdate.get().getId()).userId(userToUpdate.get().getId()).build();
                this.userDeviceMappingRepository.save(newMapping);
            }
        } else {
            if (!deviceDTO.getOwnerId().equals("0")) {
                UserDeviceMapping userDeviceMapping = UserDeviceMapping.builder()
                        .userId(Long.parseLong(deviceDTO.getOwnerId()))
                        .deviceId(deviceDTO.getId())
                        .device(deviceToUpdate.get())
                        .build();

                this.userDeviceMappingRepository.save(userDeviceMapping);
            }
        }

        deviceToUpdate.get().setDescription(deviceDTO.getDescription());
        deviceToUpdate.get().setAddress(deviceDTO.getAddress());
        deviceToUpdate.get().setMaximumEnergyProduction(deviceDTO.getMaximumEnergyProduction());

        return mapToDeviceDTO(deviceRepository.save(deviceToUpdate.get()));
    }
}
