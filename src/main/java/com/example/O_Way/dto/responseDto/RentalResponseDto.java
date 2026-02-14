package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.Rental_Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalResponseDto {

    private Long id;

    private Long userId;

    private Long vehicleId;

    private Long distance;

    private LocalDateTime rentalTime;

    private double estimateCost;

    private LocalDateTime paidAt;

    private Rental_Status rentalStatus;

    private LocationResponseDto location;
}