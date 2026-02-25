package com.example.O_Way.service.serviceImp;


import com.example.O_Way.dto.requestDto.TransactionRequestDto;
import com.example.O_Way.dto.responseDto.TransactionResponseDto;
import com.example.O_Way.model.Transaction;
import com.example.O_Way.repo.TransactionRepo;
import com.example.O_Way.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepo transactionRepository;

    @Override
    public List<TransactionResponseDto> getTransactionsByUsername(String username, TransactionRequestDto requestDto) {
        List<Transaction> transactions;

        if (requestDto.getFromDate() != null && requestDto.getToDate() != null) {
            transactions = transactionRepository.findByWallet_User_NameAndCreatedAtBetween(
                    username, requestDto.getFromDate(), requestDto.getToDate()
            );
        } else {
            transactions = transactionRepository.findByWallet_User_Name(username);
        }

        return transactions.stream()
                .map(t -> new TransactionResponseDto(
                        t.getId(),
                        t.getAmount(),
                        t.getType(),
                        t.getDirection(),
                        t.getTransactionStatus(),
                        t.getReferenceId(),
                        t.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public List<TransactionResponseDto> getAllTransactions(TransactionRequestDto requestDto) {

        List<Transaction> transactions;

        // Filter by date if provided
        if (requestDto != null &&
                requestDto.getFromDate() != null &&
                requestDto.getToDate() != null) {

            transactions = transactionRepository.findByCreatedAtBetween(
                    requestDto.getFromDate(),
                    requestDto.getToDate()
            );
        } else {
            transactions = transactionRepository.findAll();
        }

        // Convert Entity -> DTO
        return transactions.stream()
                .map(t -> new TransactionResponseDto(
                        t.getId(),
                        t.getAmount(),
                        t.getType(),
                        t.getDirection(),
                        t.getTransactionStatus(),
                        t.getReferenceId(),
                        t.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}