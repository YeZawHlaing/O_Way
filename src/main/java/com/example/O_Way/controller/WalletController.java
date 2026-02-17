package com.example.O_Way.controller;

import com.example.O_Way.model.Wallet;
import com.example.O_Way.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<?> createWallet() {

        Wallet wallet = walletService.createWalletForCurrentUser();

        return ResponseEntity.ok(wallet);
    }
}
