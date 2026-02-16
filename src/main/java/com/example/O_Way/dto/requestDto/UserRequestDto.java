package com.example.O_Way.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String roles;
}
