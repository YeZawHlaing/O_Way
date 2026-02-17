package com.example.O_Way.dto.requestDto;

import com.example.O_Way.util.status.PaymentMethod;
import com.example.O_Way.util.status.PaymentStatus;
import lombok.Data;

@Data
public class PaymentPatchRequestDto {

    private PaymentMethod method;

    private PaymentStatus status;
}