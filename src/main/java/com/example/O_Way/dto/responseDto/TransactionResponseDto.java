package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.Direction;
import com.example.O_Way.util.status.TransactionStatus;
import com.example.O_Way.util.status.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private Type type;
    private Direction direction;
    private TransactionStatus transactionStatus;
    private String referenceId;
    private LocalDateTime createdAt;
}