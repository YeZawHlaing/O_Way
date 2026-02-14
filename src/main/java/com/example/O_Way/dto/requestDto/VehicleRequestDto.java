package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.Driver_Status;
import com.example.O_Way.util.status.Vehicle_Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequestDto {

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Contact is required")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "Phone number must start with 09 and be exactly 11 digits"
    )
    private String contact;

    @NotBlank(message = "NRC is required")
    @Pattern(
            regexp = "^(1[0-4]|[1-9])/[A-Za-z]+\\([A-Z]\\)\\d{6}$",
            message = "Invalid NRC format. Must be in the format 1-14/Word(A-Z)123456"
    )
    private String nrc;

    @NotNull(message = "Vehicle status is required")
    private Vehicle_Status vehicleStatus;

    @NotNull(message = "Driver status is required")
    private Driver_Status driverStatus;

    @NotNull(message = "Location is required")
    @Valid
    private LocationRequestDto location;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequestDto address;

}