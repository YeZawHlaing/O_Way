package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;

public interface RentalService {

    public ApiResponse createRental(String username, RentalRequestDto request);

    ApiResponse getRentalById(Long id);

    ApiResponse deleteRental(Long rentalId);

    ApiResponse getAllRentals();

    ApiResponse getRentalByDriverName(String username);

    ApiResponse getRentalByCustomerName(String username);

    ApiResponse updateRentalStatusById(Long rentalId, String username, String status);
}