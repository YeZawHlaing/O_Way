package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.VehicleRequestDto;
import com.example.O_Way.dto.responseDto.ProfileResponseDto;
import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;
import com.example.O_Way.model.*;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.VehicleRepo;
import com.example.O_Way.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImp implements VehicleService {

    private final VehicleRepo vehicleRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    // =====================================================
    // CREATE VEHICLE
    // =====================================================
    @Override
    public ApiResponse createVehicle(String username, VehicleRequestDto request) {

        // 1️⃣ Find authenticated user by username
        User user = userRepo.findByName(username);

        // 2️⃣ Check if user already has vehicle (since OneToOne)
        if (vehicleRepo.existsByUser_Id(user.getId())) {
            throw new IllegalStateException("User already has a vehicle.");
        }

        // 3️⃣ Create vehicle
        Vehicle vehicle = new Vehicle();

        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setContact(request.getContact());
        vehicle.setNrc(request.getNrc());
        vehicle.setVehicleStatus(request.getVehicleStatus());
        vehicle.setDriverStatus(request.getDriverStatus());

        // 4️⃣ Map location
        if (request.getLocation() != null) {
            Location location = modelMapper.map(request.getLocation(), Location.class);
            vehicle.setLocation(location);
        }

        // 5️⃣ Map address
        if (request.getAddress() != null) {
            Address address = modelMapper.map(request.getAddress(), Address.class);
            vehicle.setAddress(address);
        }

        vehicle.setUser(user);

        vehicleRepo.save(vehicle);

        // 6️⃣ Prepare response
        VehicleResponseDto responseDto = modelMapper.map(vehicle, VehicleResponseDto.class);
        responseDto.setUserId(user.getId());

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Vehicle created successfully")
                .data(responseDto)
                .build();
    }

    // =====================================================
    // UPDATE VEHICLE
    // =====================================================
    @Override
    public ApiResponse updateVehicle(Long userId, VehicleRequestDto request) {

        Vehicle vehicle = vehicleRepo.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found for user id: " + userId));

        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setContact(request.getContact());
        vehicle.setNrc(request.getNrc());
        vehicle.setVehicleStatus(request.getVehicleStatus());
        vehicle.setDriverStatus(request.getDriverStatus());

        // Update location safely
        if (vehicle.getLocation() == null) {
            vehicle.setLocation(modelMapper.map(request.getLocation(), Location.class));
        } else {
            modelMapper.map(request.getLocation(), vehicle.getLocation());
        }

        // Update address safely
        if (vehicle.getAddress() == null) {
            vehicle.setAddress(modelMapper.map(request.getAddress(), Address.class));
        } else {
            modelMapper.map(request.getAddress(), vehicle.getAddress());
        }

        vehicleRepo.save(vehicle);

        VehicleResponseDto responseDto = modelMapper.map(vehicle, VehicleResponseDto.class);
        responseDto.setUserId(userId);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Vehicle updated successfully")
                .data(responseDto)
                .build();
    }

    // =====================================================
    // GET VEHICLE BY USER ID
    // =====================================================
    @Override
    public ApiResponse getVehicleById(Long userId) {

        Vehicle vehicle = vehicleRepo.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found for user id: " + userId));

        VehicleResponseDto responseDto = modelMapper.map(vehicle, VehicleResponseDto.class);
        responseDto.setUserId(userId);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Vehicle fetched successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse updateVehicles(String username, VehicleRequestDto request) {

        // 1️⃣ Get user
        User user = userRepo.findByName(username);


        // 2️⃣ Get vehicle of that user
        Vehicle vehicle = vehicleRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        // 3️⃣ Update simple fields (only if not null)
        if (request.getPlateNumber() != null)
            vehicle.setPlateNumber(request.getPlateNumber());

        if (request.getContact() != null)
            vehicle.setContact(request.getContact());

        if (request.getNrc() != null)
            vehicle.setNrc(request.getNrc());

        if (request.getVehicleStatus() != null)
            vehicle.setVehicleStatus(request.getVehicleStatus());

        if (request.getDriverStatus() != null)
            vehicle.setDriverStatus(request.getDriverStatus());

        // 4️⃣ Update Address (create if null, update if exists)
        if (request.getAddress() != null) {

            if (vehicle.getAddress() == null) {
                Address address = modelMapper.map(request.getAddress(), Address.class);
                vehicle.setAddress(address);
            } else {
                modelMapper.map(request.getAddress(), vehicle.getAddress());
            }
        }

        // 5️⃣ Update Location (create if null, update if exists)
        if (request.getLocation() != null) {

            if (vehicle.getLocation() == null) {
                Location location = modelMapper.map(request.getLocation(), Location.class);
                vehicle.setLocation(location);
            } else {
                modelMapper.map(request.getLocation(), vehicle.getLocation());
            }
        }

        vehicleRepo.save(vehicle);

        VehicleResponseDto responseDto = modelMapper.map(vehicle, VehicleResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Vehicle updated successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public ApiResponse deleteVehicle() {

        // 1️⃣ Get authenticated username
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        // 2️⃣ Find user
        User user = userRepo.findByName(username);


        // 3️⃣ Check if vehicle exists
        Vehicle vehicle = vehicleRepo.findByUser(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vehicle not found for this user"));

        // 4️⃣ Delete vehicle
        vehicleRepo.delete(vehicle);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Vehicle deleted successfully")
                .build();
    }

    @Override
    public List<VehicleResponseDto> getAllVehicles() {
        return vehicleRepo.findAll()
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleResponseDto.class))
                .toList();
    }

    @Override
    public ApiResponse getMyVehicle() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepo.findByName(username);

        Optional<Vehicle> vehicle= vehicleRepo.findByUserName(username);

//        Profile profile = profileRepository.findByUserName(username)
//                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        VehicleResponseDto response =
                modelMapper.map(vehicle, VehicleResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Vehicle fetched successfully")
                .data(response)
                .build();
    }

}