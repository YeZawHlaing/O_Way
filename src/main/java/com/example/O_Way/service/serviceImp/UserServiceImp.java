package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.UserRequestDto;
import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.model.Roles;
import com.example.O_Way.model.User;
import com.example.O_Way.repo.RolesRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepo userRepository;
    private final RolesRepo roleRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApiResponse createUser(UserRequestDto request) {

        Roles role = roleRepository.findByName(request.getRoles())
                .orElseThrow(() -> new EntityNotFoundException("Default role Customer not found."));

        User user = new User();
        user.setName(request.getName());
        user.setPassword(request.getPassword()); // encode later if needed
        user.setRoles(role);

        userRepository.save(user);

        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("currentUser", dto))
                .message("User account created Successfully.")
                .build();
    }

    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

}