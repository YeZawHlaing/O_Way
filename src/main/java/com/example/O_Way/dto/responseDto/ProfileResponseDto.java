package com.example.O_Way.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private String contact;

    private LocalDate dob;

    private String gender;

    private String profilePic;

    private LocationResponseDto location;
}