package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private Long rentalId;

    private BigDecimal amount;

    private PaymentMethod method;
}