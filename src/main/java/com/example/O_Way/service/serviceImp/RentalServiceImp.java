package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;
import com.example.O_Way.dto.responseDto.RentalResponseDto;
import com.example.O_Way.model.*;
import com.example.O_Way.repo.RentalRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.VehicleRepo;
import com.example.O_Way.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalServiceImp implements RentalService {

    private final RentalRepo rentalRepo;
    private final UserRepo userRepo;
    private final VehicleRepo vehicleRepo;
    private final ModelMapper modelMapper;

    private static final double PRICE_PER_KM = 500;

    // =========================================
    // CREATE RENTAL
    // =========================================
    @Override
    public ApiResponse createRental(RentalRequestDto request) {

        // 1️⃣ Find user
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2️⃣ Find vehicle
        Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        // 3️⃣ Create rental
        Rental rental = new Rental();

        rental.setUser(user);
        rental.setVehicle(vehicle);
        rental.setDistance(request.getDistance());
        rental.setRental_time(request.getRentalTime());
        rental.setRentalStatus(request.getRentalStatus());

        // 4️⃣ Auto calculate estimate cost
        double estimateCost = request.getDistance() * PRICE_PER_KM;
        rental.setEstimate_cost(estimateCost);

        // 5️⃣ If paid status → set paidAt
        if (request.getRentalStatus().name().equals("PAID")) {
            rental.setPaid_at(LocalDateTime.now());
        }

        // 6️⃣ Set location
        if (request.getLocation() != null) {
            Location location = modelMapper.map(request.getLocation(), Location.class);
            rental.setLocation(location);
        }

        rentalRepo.save(rental);

        // 7️⃣ Prepare response
        RentalResponseDto responseDto = modelMapper.map(rental, RentalResponseDto.class);
        responseDto.setUserId(user.getId());
        responseDto.setVehicleId(vehicle.getId());

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental created successfully")
                .data(responseDto)
                .build();
    }

    // =========================================
    // GET RENTAL BY ID
    // =========================================
    @Override
    public ApiResponse getRentalById(Long rentalId) {

        Rental rental = rentalRepo.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        RentalResponseDto responseDto = modelMapper.map(rental, RentalResponseDto.class);
        responseDto.setUserId(rental.getUser().getId());
        responseDto.setVehicleId(rental.getVehicle().getId());

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental fetched successfully")
                .data(responseDto)
                .build();
    }
}