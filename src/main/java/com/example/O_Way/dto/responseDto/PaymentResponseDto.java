package com.example.O_Way.dto.responseDto;

import com.example.O_Way.util.status.PaymentMethod;
import com.example.O_Way.util.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private Long id;

    private Long rentalId;

    private BigDecimal amount;

    private PaymentMethod method;

    private PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long driverWalletId;
}