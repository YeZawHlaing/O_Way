package com.example.O_Way.service;

import com.example.O_Way.dto.requestDto.PaymentPatchRequestDto;
import com.example.O_Way.dto.requestDto.PaymentRequestDto;
import com.example.O_Way.dto.responseDto.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto createPayment(PaymentRequestDto request);

    PaymentResponseDto updatePayment(Long paymentId, PaymentPatchRequestDto request);
}