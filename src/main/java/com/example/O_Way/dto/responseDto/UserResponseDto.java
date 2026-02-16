package com.example.O_Way.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;

    private String name;

    private String password;

    private RolesResponseDto roles;

    private ProfileResponseDto profile;

    private boolean hasProfile;
}
