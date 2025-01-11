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
public class RoleDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory!")
    private String name;
}
