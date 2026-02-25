package com.example.O_Way.service;

import com.example.O_Way.dto.requestDto.TransactionRequestDto;
import com.example.O_Way.dto.responseDto.TransactionResponseDto;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDto> getTransactionsByUsername(String username, TransactionRequestDto requestDto);
    List<TransactionResponseDto> getAllTransactions(TransactionRequestDto requestDto);
}
