package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.RentalRequestDto;

public interface RentalService {

    ApiResponse createRental(RentalRequestDto request);

    ApiResponse getRentalById(Long rentalId);
}