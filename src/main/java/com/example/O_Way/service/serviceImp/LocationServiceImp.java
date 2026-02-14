package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.LocationRequestDto;
import com.example.O_Way.model.Location;
import com.example.O_Way.repo.LocationRepo;
import com.example.O_Way.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImp implements LocationService {

    private final LocationRepo locationRepo;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createLocation(LocationRequestDto request) {
        // Map DTO to entity
        Location location = modelMapper.map(request, Location.class);

        // Save entity
        Location savedLocation = locationRepo.save(location);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Location created successfully")
                .data(savedLocation)
                .build();
    }

    @Override
    @Transactional
    public List<Location> getLocation() {
        return locationRepo.findAll();
    }
}