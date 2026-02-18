package com.example.O_Way.controller;

import com.example.O_Way.dto.requestDto.TransactionRequestDto;
import com.example.O_Way.dto.responseDto.TransactionResponseDto;
import com.example.O_Way.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponseDto> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            TransactionRequestDto requestDto
    ) {
        String name = userDetails.getUsername(); // this is actually user's 'name' in your User entity
        return transactionService.getTransactionsByUsername(name, requestDto);
    }
}
