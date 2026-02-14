package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.Rental_Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Distance is required")
    private Long distance;

    @NotNull(message = "Rental time is required")
    private LocalDateTime rentalTime;

    private LocalDateTime paidAt;

    @NotNull(message = "Rental status is required")
    private Rental_Status rentalStatus;

    @Valid
    private LocationRequestDto location;
}