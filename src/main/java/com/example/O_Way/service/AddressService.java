package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.AddressRequestDto;
import com.example.O_Way.model.Address;

import java.util.List;

public interface AddressService {

    public ApiResponse createAddress(AddressRequestDto request);
    List<Address> getAddress();
}
