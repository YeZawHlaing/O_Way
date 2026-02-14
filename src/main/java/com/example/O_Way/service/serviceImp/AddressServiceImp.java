package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.AddressRequestDto;
import com.example.O_Way.model.Address;
import com.example.O_Way.repo.AddressRepo;
import com.example.O_Way.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImp implements AddressService {

    private final AddressRepo addressRepo;

    // -------------------------------
    // Create Address
    // -------------------------------
    @Override
    public ApiResponse createAddress(AddressRequestDto request) {

        Address address = new Address();

        address.setCity(request.getCity());
        address.setTownship(request.getTownship());
        address.setRoad(request.getRoad());
        address.setStreet(request.getStreet());

        addressRepo.save(address);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .message("Address created successfully")
                .data(address)
                .build();
    }

    // -------------------------------
    // Get All Address
    // -------------------------------
    @Override
    public List<Address> getAddress() {
        return addressRepo.findAll();
    }
}