package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.LocationRequestDto;
import com.example.O_Way.model.Location;
import com.example.O_Way.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/createLocation")
    public ResponseEntity<ApiResponse> createLocation(
            @Valid @RequestBody LocationRequestDto request
    ) {
        ApiResponse response = locationService.createLocation(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("getLocation")
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getLocation();
        return ResponseEntity.ok(locations);
    }
}