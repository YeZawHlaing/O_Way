package com.example.O_Way.controller;


import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;
import com.example.O_Way.model.Roles;
import com.example.O_Way.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VehicleService vehicleService;
    private final UserService userService;
    private final RolesService rolesService;
    private final ProfileService profileService;
    private final RentalService rentalService;

    @PostMapping("/roles")
    public ResponseEntity<Roles> createRole(@RequestBody Roles role) {

        Roles savedRole = rolesService.CreateRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/getRoles")
    public ResponseEntity<List<Roles>> getRoles() {
        return ResponseEntity.ok(rolesService.getRoles());
    }
    @GetMapping("/getProfiles")
    public ResponseEntity<ApiResponse> getProfile(Authentication authentication) {

        String username = authentication.getName(); // from JWT

        ApiResponse response = profileService.getProfileByUsername(username);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteVehicle")
    public ResponseEntity<ApiResponse> deleteVehicle() {
        return ResponseEntity.ok(vehicleService.deleteVehicle());
    }

    @GetMapping("getUser")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("getVehicle")
    public ResponseEntity<List<VehicleResponseDto>> getVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/getRentals")
    public ResponseEntity<ApiResponse> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }
}
