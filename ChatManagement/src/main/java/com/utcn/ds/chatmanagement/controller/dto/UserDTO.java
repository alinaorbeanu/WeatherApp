package com.utcn.ds.chatmanagement.controller.dto;


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

    private String name;

    private String email;

    private String role;

    @Override
    public int compareTo(UserDTO userDTO) {
        return id.compareTo(userDTO.id);
    }
}
