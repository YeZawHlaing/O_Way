package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;
import com.example.O_Way.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // ===============================
    // CREATE RENTAL
    // ===============================
    @PostMapping()
    public ResponseEntity<ApiResponse> createRental(
            @Valid @RequestBody RentalRequestDto request
    ) {
        return ResponseEntity.ok(rentalService.createRental(request));
    }

    // ===============================
    // GET RENTAL BY ID
    // ===============================
    @GetMapping("/{rentalId}")
    public ResponseEntity<ApiResponse> getRentalById(
            @PathVariable Long rentalId
    ) {
        return ResponseEntity.ok(rentalService.getRentalById(rentalId));
    }
}