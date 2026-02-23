package com.example.O_Way.service;

import com.example.O_Way.dto.requestDto.TransferRequestDto;
import com.example.O_Way.dto.responseDto.TransferResponseDto;

public interface TransferService {

    TransferResponseDto transfer(TransferRequestDto request);
    public TransferResponseDto transferForRental(Long rentalId);
}