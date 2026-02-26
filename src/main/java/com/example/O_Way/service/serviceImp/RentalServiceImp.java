package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;
import com.example.O_Way.dto.responseDto.ProfileResponseDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalServiceImp implements RentalService {

    private final RentalRepo rentalRepo;
    private final UserRepo userRepo;
    private final VehicleRepo vehicleRepo;
    private final ModelMapper modelMapper;

    private static final double PRICE_PER_KM = 1100;
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

        // 3️⃣ Check driver availability (NOT vehicle status)
        if (vehicle.getDriverStatus() != Driver_Status.AVAILABLE) {
            throw new IllegalStateException("Driver is not available");
        }

        if (vehicle.getVehicleStatus() != Vehicle_Status.ACCEPTED) {
            throw new IllegalStateException("Vehicle can't be rent!");
        }

        // 4️⃣ Driver is vehicle owner (realistic logic)
        User driver = vehicle.getUser();

        Wallet driverWallet = driver.getWallet();

        if (driverWallet == null) {
            throw new IllegalStateException("Driver wallet not found");
        }

        Rental rental = new Rental();

        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setDriver(driver);

        rental.setDistance(request.getDistance());
        rental.setRental_time(LocalDateTime.now());
        rental.setRentalStatus(Rental_Status.PENDING);

        // 5️⃣ Calculate cost
        double estimateCost = request.getDistance() * PRICE_PER_KM;
        System.out.println(rental.getDistance());
        System.out.println(estimateCost);
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
        responseDto.setDriverWalletId(driverWallet.getId());
        responseDto.setEstimateCost(rental.getEstimate_cost());

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

    @Override
    public ApiResponse deleteRental(Long rentalId) {

        // 1️⃣ Get logged-in username from JWT
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        // 2️⃣ Find rental by ID AND authenticated customer
        Rental rental = rentalRepo
                .findByIdAndCustomer_Name(rentalId, username)
                .orElseThrow(() ->
                        new EntityNotFoundException("Rental not found or not yours"));

        // 3️⃣ Optional: Allow delete only if status is PENDING
        if (rental.getRentalStatus() != Rental_Status.PENDING) {
            throw new IllegalStateException("Only pending rentals can be deleted");
        }

        // 4️⃣ Delete
        rentalRepo.delete(rental);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental deleted successfully")
                .build();
    }

    @Override
    public ApiResponse getAllRentals() {

        List<Rental> rentals = rentalRepo.findAll();

        List<RentalResponseDto> responseList = rentals.stream().map(rental -> {

            RentalResponseDto dto = modelMapper.map(rental, RentalResponseDto.class);

            dto.setCustomerId(rental.getCustomer().getId());
            dto.setVehicleId(rental.getVehicle().getId());

            if (rental.getDriver() != null) {
                dto.setDriverId(rental.getDriver().getId());
            }

            return dto;

        }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("All rentals fetched successfully")
                .data(responseList)
                .build();
    }


    @Override
    public ApiResponse getRentalByDriverName(String username) {

        // 1️⃣ Find driver by authenticated username
        User driver = userRepo.findByName(username);
        if (driver == null) {
            return ApiResponse.builder()
                    .success(0)
                    .code(404)
                    .message("Driver not found")
                    .build();
        }

        // 2️⃣ Get vehicle (one driver = one vehicle)
        Vehicle vehicle = vehicleRepo.findByUser(driver)
                .orElseThrow(() -> new RuntimeException("Vehicle not found for driver"));

        // 3️⃣ Get all rentals for this vehicle
        List<Rental> rentals = rentalRepo.findByVehicleId(vehicle.getId());

        // 4️⃣ Map Rental → RentalResponseDto
        List<RentalResponseDto> rentalDtos = rentals.stream().map(rental -> {
            RentalResponseDto dto = modelMapper.map(rental, RentalResponseDto.class);

            // Set vehicle & driver
            dto.setVehicleId(vehicle.getId());
            dto.setDriverId(driver.getId());
            dto.setEstimateCost(rental.getEstimate_cost());

            // Map customer data manually
            ProfileResponseDto customerDto = new ProfileResponseDto();
            customerDto.setId(rental.getCustomer().getId());
            customerDto.setFullName(rental.getCustomer().getName());
            customerDto.setEmail(rental.getCustomer().getProfile() != null ?
                    rental.getCustomer().getProfile().getEmail() : null);
            customerDto.setContact(rental.getCustomer().getProfile() != null ?
                    rental.getCustomer().getProfile().getContact() : null);

//            dto.setCustomer(customerDto);
            dto.setProfile(customerDto);

            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(rentalDtos)
                .message("Driver rentals fetched successfully")
                .build();
    }

    @Override
    public ApiResponse getRentalByCustomerName(String username) {

        // 1️⃣ Find customer
        User customer = userRepo.findByName(username);

        if (customer == null) {
            return ApiResponse.builder()
                    .success(0)
                    .code(404)
                    .message("Customer not found")
                    .build();
        }

        // 2️⃣ Get rentals by customer
        List<Rental> rentals = rentalRepo.findByCustomer(customer);

        if (rentals.isEmpty()) {
            return ApiResponse.builder()
                    .success(1)
                    .code(200)
                    .message("No rentals found")
                    .data(List.of())
                    .build();
        }

        // 3️⃣ Map to DTO
        List<RentalResponseDto> rentalDtos = rentals.stream().map(rental -> {

            RentalResponseDto dto = modelMapper.map(rental, RentalResponseDto.class);

            // Set IDs manually (safe way)
            dto.setCustomerId(customer.getId());
            dto.setVehicleId(rental.getVehicle().getId());
            dto.setDriverId(rental.getVehicle().getUser().getId());
            dto.setEstimateCost(rental.getEstimate_cost());

            return dto;

        }).toList();

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(rentalDtos)
                .message("Customer rentals fetched successfully")
                .build();
    }

    @Override
    public ApiResponse updateRentalStatusById(Long rentalId,
                                          String username,
                                          String status) {

        // 1️⃣ Find rental
        Rental rental = rentalRepo.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // 2️⃣ Validate driver ownership
        User driver = rental.getVehicle().getUser();

        if (!driver.getName().equals(username)) {
            return ApiResponse.builder()
                    .success(0)
                    .code(403)
                    .message("You are not authorized to update this rental")
                    .build();
        }

        // 3️⃣ Validate current status
        if (rental.getRentalStatus() != Rental_Status.PENDING) {
            return ApiResponse.builder()
                    .success(0)
                    .code(400)
                    .message("Only PENDING rentals can be updated")
                    .build();
        }

        // 4️⃣ Convert String → Enum safely
        Rental_Status newStatus;

        try {
            newStatus = Rental_Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ApiResponse.builder()
                    .success(0)
                    .code(400)
                    .message("Invalid rental status")
                    .build();
        }

        // 5️⃣ Only allow ACCEPTED or DECLINED
        if (newStatus != Rental_Status.ACCEPTED &&
                newStatus != Rental_Status.DECLINED) {

            return ApiResponse.builder()
                    .success(0)
                    .code(400)
                    .message("Status must be ACCEPTED or DECLINED")
                    .build();
        }

        // 6️⃣ Update status
        rental.setRentalStatus(newStatus);
        rentalRepo.save(rental);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Rental status updated to " + newStatus)
                .data(rental.getId())
                .build();
    }
}