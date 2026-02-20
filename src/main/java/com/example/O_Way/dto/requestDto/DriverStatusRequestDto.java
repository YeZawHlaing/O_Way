package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.Driver_Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverStatusRequestDto {

    @NotNull
    private Driver_Status driverStatus;
}
