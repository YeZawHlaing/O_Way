package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.VehicleRequestDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;
import com.example.O_Way.model.Address;
import com.example.O_Way.model.Location;
import com.example.O_Way.model.User;
import com.example.O_Way.model.Vehicle;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.VehicleRepo;
import com.example.O_Way.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ApiResponse createVehicle(Long userId, VehicleRequestDto request) {

        // 1️⃣ Find user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 2️⃣ Check if vehicle already exists for this user (OneToOne safety)
        if (vehicleRepo.findByUser_Id(userId).isPresent()) {
            throw new IllegalStateException("User already has a vehicle");
        }

        // 3️⃣ Create vehicle entity
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setContact(request.getContact());
        vehicle.setNrc(request.getNrc());
        vehicle.setVehicleStatus(request.getVehicleStatus());
        vehicle.setDriverStatus(request.getDriverStatus());

        // 4️⃣ Map Location
        Location location = modelMapper.map(request.getLocation(), Location.class);
        vehicle.setLocation(location);

        // 5️⃣ Map Address
        Address address = modelMapper.map(request.getAddress(), Address.class);
        vehicle.setAddress(address);

        // 6️⃣ Set relationship
        vehicle.setUser(user);

        // 7️⃣ Save
        vehicleRepo.save(vehicle);

        // 8️⃣ Prepare response
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
}