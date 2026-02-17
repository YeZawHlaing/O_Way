package com.example.O_Way.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDto {

    private Long id; // match Wallet entity field name
    private BigDecimal balance;
    private UserResponseDto user;
}