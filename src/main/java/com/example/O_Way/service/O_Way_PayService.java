package com.example.O_Way.service;

import com.example.O_Way.dto.requestDto.O_Way_PayRequestDto;
import com.example.O_Way.dto.responseDto.O_Way_PayResponseDto;

public interface O_Way_PayService {

    O_Way_PayResponseDto pay(O_Way_PayRequestDto request);
}