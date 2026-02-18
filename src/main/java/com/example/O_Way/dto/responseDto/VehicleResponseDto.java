package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.Driver_Status;
import com.example.O_Way.util.status.Vehicle_Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponseDto {

    private Long id;

    private String plateNumber;

    private String contact;

    private String nrc;

    private Vehicle_Status vehicleStatus;

    private Driver_Status driverStatus;

    private LocationResponseDto location;

    private AddressResponseDto address;

    private UserResponseDto user;

    private Long userId;
}