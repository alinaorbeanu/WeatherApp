package com.utcn.ds.devicesmanagement.controller.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterRequestDTO {

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotBlank(message = "Email is mandatory!")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid!")
    private String email;

    @NotBlank(message = "Password is mandatory!")
    private String password;

    @NotBlank(message = "Role is mandatory!")

    private RoleDTO roleDTO;

    private String role;
}
