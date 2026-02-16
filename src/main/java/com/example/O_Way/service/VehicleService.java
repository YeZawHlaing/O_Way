package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.VehicleRequestDto;

public interface VehicleService {

    ApiResponse createVehicle(String username, VehicleRequestDto request);

    public ApiResponse updateVehicle(final Long userId,final VehicleRequestDto vehicleRequest);

    public ApiResponse getVehicleById(final Long userId);

    ApiResponse updateVehicles(String username, VehicleRequestDto request);

}
