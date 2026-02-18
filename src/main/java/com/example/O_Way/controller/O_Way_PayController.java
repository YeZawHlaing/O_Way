package com.example.O_Way.controller;

import com.example.O_Way.dto.requestDto.O_Way_PayRequestDto;
import com.example.O_Way.dto.responseDto.O_Way_PayResponseDto;
import com.example.O_Way.service.O_Way_PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class O_Way_PayController {

    private final O_Way_PayService oWayPayService;

    @PostMapping("/o_way_pay")
    public ResponseEntity<O_Way_PayResponseDto> pay(@RequestBody O_Way_PayRequestDto request) {
        return ResponseEntity.ok(oWayPayService.pay(request));
    }
}