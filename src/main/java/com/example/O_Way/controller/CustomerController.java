package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.*;
import com.example.O_Way.dto.responseDto.*;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final WalletService walletService;
    private final PaymentService paymentService;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final ProfileService profileService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final UserService userService;

    @GetMapping("/getVehicle")
    public ResponseEntity<List<VehicleResponseDto>> getVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

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
    @PostMapping("/profile")
    public ResponseEntity<ApiResponse> createProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileRequestDto request
    ) {

        String username = userDetails.getUsername();

        ApiResponse response = profileService.createProfile(username, request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/myWallet")
    public ResponseEntity<WalletResponseDto> getMyWallet() {
        WalletResponseDto walletDto = walletService.getMyWallet();
        return ResponseEntity.ok(walletDto);
    }

    @GetMapping("/rentals")
    public ResponseEntity<ApiResponse> getMyRentals(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        ApiResponse response =
                rentalService.getRentalByCustomerName(username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/agents")
    public ResponseEntity<ApiResponse> getAllAgents() {

        ApiResponse response = userService.getAllAgents();

        return ResponseEntity.ok(response);
    }
    @PostMapping("/rental/{rentalId}/transfer")
    public ResponseEntity<TransferResponseDto> transferForRental(
            @PathVariable Long rentalId) {

        TransferResponseDto response =
                transferService.transferForRental(rentalId);

        return ResponseEntity.ok(response);
    }
}
