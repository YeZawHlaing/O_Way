package com.example.O_Way.controller;

import com.example.O_Way.dto.requestDto.PaymentPatchRequestDto;
import com.example.O_Way.dto.requestDto.PaymentRequestDto;
import com.example.O_Way.dto.responseDto.PaymentResponseDto;
import com.example.O_Way.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @RequestBody PaymentRequestDto request) {

        return ResponseEntity.ok(
                paymentService.createPayment(request)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentPatchRequestDto request) {

        return ResponseEntity.ok(
                paymentService.updatePayment(id, request)
        );
    }
}