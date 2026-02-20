package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.Driver_Status;
import com.example.O_Way.util.status.Vehicle_Status;
import lombok.Data;

@Data
public class VehicleUpdateDto {
    private String plateNumber;
    private String contact;
    private String nrc;
    private Vehicle_Status vehicleStatus;
    private Driver_Status driverStatus;
    private AddressRequestDto address;
    private LocationRequestDto location;
}
