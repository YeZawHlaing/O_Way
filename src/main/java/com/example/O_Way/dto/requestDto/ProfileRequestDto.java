package com.example.O_Way.dto.requestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileRequestDto {

    // Required (entity nullable = false)
    @NotBlank(message = "Full name must not be empty")
    private String fullName;

    // Required (entity nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    // OPTIONAL (entity nullable = true, unique = true)
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "Phone number must start with 09 and be exactly 11 digits"
    )
    private String contact;

    // Required (entity nullable = false)
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    // Optional (entity nullable = true)
    private String profilePic;

    // Optional (entity has no NOT NULL constraint)
    private String gender;

    // Optional (entity does not specify nullable = false)
    @Valid
    private LocationRequestDto locationRequestDto;
}