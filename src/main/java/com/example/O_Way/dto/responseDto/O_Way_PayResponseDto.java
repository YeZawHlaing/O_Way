package com.example.O_Way.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class O_Way_PayResponseDto {

    private Long id;
    private Long paymentId;
    private Long transferId;
}