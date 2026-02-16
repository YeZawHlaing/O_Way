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

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long distance;

    @NotNull
    private Double pickupLatitude;

    @NotNull
    private Double pickupLongitude;

    @NotNull
    private Double dropLatitude;

    @NotNull
    private Double dropLongitude;

}