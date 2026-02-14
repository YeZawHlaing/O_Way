package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.AddressRequestDto;
import com.example.O_Way.model.Address;
import com.example.O_Way.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // -------------------------------
    // Create Address
    // -------------------------------
    @PostMapping
    public ResponseEntity<ApiResponse> createAddress(
            @Valid @RequestBody AddressRequestDto request
    ) {
        ApiResponse response = addressService.createAddress(request);
        return ResponseEntity.ok(response);
    }

    // -------------------------------
    // Get All Addresses
    // -------------------------------
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddress() {
        return ResponseEntity.ok(addressService.getAddress());
    }
}