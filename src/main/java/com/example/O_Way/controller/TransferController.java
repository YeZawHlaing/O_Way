package com.example.O_Way.controller;

import com.example.O_Way.dto.requestDto.TransferRequestDto;
import com.example.O_Way.dto.responseDto.TransferResponseDto;
import com.example.O_Way.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDto> transfer(
            @RequestBody TransferRequestDto request) {

        return ResponseEntity.ok(
                transferService.transfer(request)
        );
    }
}