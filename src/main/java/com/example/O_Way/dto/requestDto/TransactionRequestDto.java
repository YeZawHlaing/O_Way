package com.example.O_Way.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRequestDto {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}