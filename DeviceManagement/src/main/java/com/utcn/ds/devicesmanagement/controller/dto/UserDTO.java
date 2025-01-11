package com.utcn.ds.devicesmanagement.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserDTO implements Comparable<UserDTO> {

    private Long id;

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotBlank(message = "Email is mandatory!")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid!")
    private String email;

    @NotBlank(message = "Password is mandatory!")
    private String password;

    @NotNull
    private RoleDTO roleDTO;

    @NotNull
    private String role;

    @Override
    public int compareTo(UserDTO userDTO) {
        return id.compareTo(userDTO.id);
    }
}
