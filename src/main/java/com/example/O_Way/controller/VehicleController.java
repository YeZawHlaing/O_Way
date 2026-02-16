package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.VehicleRequestDto;
import com.example.O_Way.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse> createVehicle(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VehicleRequestDto request
    ) {

        String username = userDetails.getUsername();

        return ResponseEntity.ok(
                vehicleService.createVehicle(username, request)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateVehicle(
            @RequestParam Long userId,
            @Valid @RequestBody VehicleRequestDto request
    ) {
        return ResponseEntity.ok(vehicleService.updateVehicle(userId, request));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> updateVehicle(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VehicleRequestDto request
    ) {

        String username = userDetails.getUsername();

        return ResponseEntity.ok(
                vehicleService.updateVehicles(username, request)
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getVehicle(@RequestParam Long userId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(userId));
    }
}