package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.UserRequestDto;
import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.model.User;
import com.example.O_Way.service.UserService;
import com.example.O_Way.util.response.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody final UserRequestDto userRequest,
            final HttpServletRequest request
    ) {
        final ApiResponse response = this.userService.createUser(userRequest);
        return ResponseUtils.buildResponse(request, response);
    }

@GetMapping("getUser")
public ResponseEntity<List<UserResponseDto>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
}
}
