package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.LocationRequestDto;
import com.example.O_Way.model.Location;


import java.util.List;

public interface LocationService {

    public ApiResponse createLocation(LocationRequestDto request);
    List<Location> getLocation();
}
