package com.example.O_Way.controller;


import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.TransactionRequestDto;
import com.example.O_Way.dto.requestDto.TransferRequestDto;
import com.example.O_Way.dto.responseDto.TransactionResponseDto;
import com.example.O_Way.dto.responseDto.TransferResponseDto;
import com.example.O_Way.dto.responseDto.VehicleResponseDto;
import com.example.O_Way.dto.responseDto.WalletResponseDto;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final WalletService walletService;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;

    @GetMapping("/transaction")
    public List<TransactionResponseDto> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            TransactionRequestDto requestDto
    ) {
        String name = userDetails.getUsername(); // this is actually user's 'name' in your User entity
        return transactionService.getTransactionsByUsername(name, requestDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDto> transfer(
            @RequestBody TransferRequestDto request) {

        return ResponseEntity.ok(
                transferService.transfer(request)
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

    @GetMapping("getVehicle")
    public ResponseEntity<List<VehicleResponseDto>> getVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/myProfile")
    public ResponseEntity<ApiResponse> getMyVehicle() {
        return ResponseEntity.ok(vehicleService.getMyVehicle());
    }

    @GetMapping("/rentals")
    public ResponseEntity<ApiResponse> getMyRentals(
            @AuthenticationPrincipal UserDetails userDetails) {

        String driverName = userDetails.getUsername();
        ApiResponse response = rentalService.getRentalByDriverName(driverName);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/rentals/{rentalId}/status")
    public ResponseEntity<ApiResponse> updateRentalStatus(
            @PathVariable Long rentalId,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        ApiResponse response =
                rentalService.updateRentalStatusById(rentalId, username, status);

        return ResponseEntity.ok(response);
    }
}
