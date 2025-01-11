package com.utcn.ds.usermanagement.controller.dto;

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
public class DeviceDTO {

    private Long id;

    @NotBlank(message = "Description is mandatory!")
    private String description;

    @NotBlank(message = "Address is mandatory!")
    private String address;

    @NotBlank(message = "Maximum Energy Production is mandatory!")
    private Long maximumEnergyProduction;
}
