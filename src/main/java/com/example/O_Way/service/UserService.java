package com.example.O_Way.service;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.common.response.PaginatedApiResponse;
import com.example.O_Way.dto.requestDto.GetAllUserRequest;
import com.example.O_Way.dto.requestDto.UserRequestDto;
import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.model.Roles;
import com.example.O_Way.model.User;

import java.util.List;

public interface UserService {

    public ApiResponse createUser(UserRequestDto request);
    List<User> getUser();
}
