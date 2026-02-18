package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.TransferStatus;
import com.example.O_Way.util.status.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDto {

    private Long id;
    private Long fromWalletId;
    private Long toWalletId;
    private BigDecimal amount;
    private TransferStatus transferStatus;
    private Type type;
    private LocalDateTime createdAt;
}