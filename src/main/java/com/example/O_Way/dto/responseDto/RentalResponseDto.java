package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.Rental_Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalResponseDto {

    private Long id;
    private Long customerId;
    private Long driverId;
    private Long vehicleId;

    private double distance;
    private double estimateCost;

    private LocalDateTime rentalTime;
    private LocalDateTime paidAt;

    private Rental_Status rentalStatus;

    private Double pickupLatitude;
    private Double pickupLongitude;

    private Double dropLatitude;
    private Double dropLongitude;

    private Long driverWalletId;

    private ProfileResponseDto profile;
    private VehicleResponseDto vehicle;
}