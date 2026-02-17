package com.example.O_Way.controller;

import com.example.O_Way.dto.responseDto.WalletResponseDto;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final WalletService walletService;

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
