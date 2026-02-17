package com.example.O_Way.controller;

import com.example.O_Way.dto.requestDto.PaymentPatchRequestDto;
import com.example.O_Way.dto.requestDto.PaymentRequestDto;
import com.example.O_Way.dto.responseDto.PaymentResponseDto;
import com.example.O_Way.dto.responseDto.WalletResponseDto;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.service.PaymentService;
import com.example.O_Way.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final WalletService walletService;
    private final PaymentService paymentService;

    @PostMapping("/payment")
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

    @PostMapping("/create")
    public ResponseEntity<?> createWallet() {

        Wallet wallet = walletService.createWalletForCurrentUser();

        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/myWallet")
    public ResponseEntity<WalletResponseDto> getMyWallet() {
        WalletResponseDto walletDto = walletService.getMyWallet();
        return ResponseEntity.ok(walletDto);
    }
}
