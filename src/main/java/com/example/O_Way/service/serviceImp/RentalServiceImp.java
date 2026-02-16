package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;
import com.example.O_Way.dto.responseDto.RentalResponseDto;
import com.example.O_Way.model.*;
import com.example.O_Way.repo.RentalRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.VehicleRepo;
import com.example.O_Way.service.RentalService;
import com.example.O_Way.util.status.Driver_Status;
import com.example.O_Way.util.status.Rental_Status;
import com.example.O_Way.util.status.Vehicle_Status;
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
    public ApiResponse createRental(String username, RentalRequestDto request) {

        // 1️⃣ Get authenticated customer
        User customer = userRepo.findByName(username);

        // 2️⃣ Get vehicle
        Vehicle vehicle = vehicleRepo.findById(request.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

//        // 3️⃣ Check vehicle availability
//        if (vehicle.getVehicleStatus() != Vehicle_Status.AVAILABLE) {
//            throw new IllegalStateException("Vehicle is not available");
//        }

        // 3️⃣ Check driver availability (NOT vehicle status)
        if (vehicle.getDriverStatus() != Driver_Status.AVAILABLE) {
            throw new IllegalStateException("Driver is not available");
        }

        // 4️⃣ Driver is vehicle owner (realistic logic)
        User driver = vehicle.getUser();

        Rental rental = new Rental();

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setDriver(driver);

        rental.setDistance(request.getDistance());
        rental.setRental_time(LocalDateTime.now());
        rental.setRentalStatus(Rental_Status.PENDING);

        // 5️⃣ Calculate cost
        double estimateCost = request.getDistance() * PRICE_PER_KM;
        rental.setEstimate_cost(estimateCost);

        rental.setPaid_at(null);

        // 6️⃣ Snapshot pickup/drop
        rental.setPickupLatitude(request.getPickupLatitude());
        rental.setPickupLongitude(request.getPickupLongitude());
        rental.setDropLatitude(request.getDropLatitude());
        rental.setDropLongitude(request.getDropLongitude());

        // 7️⃣ Update vehicle status
//        vehicle.setVehicleStatus(VehicleStatus.IN_USE);
        vehicle.setDriverStatus(Driver_Status.ASSIGNED);

        rentalRepo.save(rental);

        RentalResponseDto responseDto = modelMapper.map(rental, RentalResponseDto.class);
        responseDto.setCustomerId(customer.getId());
        responseDto.setVehicleId(vehicle.getId());
        responseDto.setDriverId(driver.getId());

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental requested successfully")
                .data(responseDto)
                .build();
    }
    // =========================================
    // GET RENTAL BY ID
    // =========================================
    @Override
    public ApiResponse getRentalById(Long id) {

        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        RentalResponseDto responseDto = modelMapper.map(rental, RentalResponseDto.class);
        responseDto.setCustomerId(rental.getCustomer().getId());
        responseDto.setVehicleId(rental.getVehicle().getId());

        if (rental.getDriver() != null) {
            responseDto.setDriverId(rental.getDriver().getId());
        }

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental fetched successfully")
                .data(responseDto)
                .build();
    }
}