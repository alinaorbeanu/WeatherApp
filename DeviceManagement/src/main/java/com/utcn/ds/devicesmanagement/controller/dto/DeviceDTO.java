package com.utcn.ds.devicesmanagement.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO implements Comparable<DeviceDTO> {

    private Long id;

    @NotBlank(message = "Description is mandatory!")
    private String description;

    @NotBlank(message = "Address is mandatory!")
    private String address;

    @NotBlank(message = "Number of devices is mandatory!")
    private Long deviceNumber;

    @NotBlank(message = "Maximum Energy Production is mandatory!")
    private Long maximumEnergyProduction;

    private String owner;

    private String ownerId;

    @Override
    public int compareTo(DeviceDTO deviceDTO) {
        return id.compareTo(deviceDTO.id);
    }
}
