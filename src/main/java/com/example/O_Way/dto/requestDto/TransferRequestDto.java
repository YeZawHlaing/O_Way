package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.Type;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    private Long toWalletId;

    private BigDecimal amount;

    private Type type;
}