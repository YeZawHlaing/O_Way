package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;
import com.example.O_Way.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // CREATE RENTAL
    @PostMapping
    public ResponseEntity<ApiResponse> createRental(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody RentalRequestDto request
    ) {
        return ResponseEntity.ok(
                rentalService.createRental(userDetails.getUsername(), request)
        );
    }

    // GET RENTAL BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRentalById(@PathVariable Long id) {

        ApiResponse response = rentalService.getRentalById(id);
        return ResponseEntity.ok(response);
    }
}