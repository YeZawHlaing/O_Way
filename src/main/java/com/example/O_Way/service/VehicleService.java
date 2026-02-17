package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.VehicleRequestDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;

import java.util.List;

public interface VehicleService {

    ApiResponse createVehicle(String username, VehicleRequestDto request);

    public ApiResponse updateVehicle(final Long userId,final VehicleRequestDto vehicleRequest);

    public ApiResponse getVehicleById(final Long userId);

    ApiResponse updateVehicles(String username, VehicleRequestDto request);

    ApiResponse deleteVehicle();

    List<VehicleResponseDto> getAllVehicles();






}
