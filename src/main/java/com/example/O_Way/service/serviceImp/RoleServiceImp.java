package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.responseDto.ProfileResponseDto;
import com.example.O_Way.dto.responseDto.RolesResponseDto;
import com.example.O_Way.model.Profile;
import com.example.O_Way.model.Roles;
import com.example.O_Way.repo.RolesRepo;
import com.example.O_Way.service.RolesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImp implements RolesService {

    private final RolesRepo rolesRepository;
    private final ModelMapper modelMapper;

    @Override
    public Roles CreateRole(Roles role) {
        rolesRepository.findByName(role.getName())
                .ifPresent(r -> {
                    throw new RuntimeException("Role already exists");
                });

        return rolesRepository.save(role);
    }

    @Override
    public List<Roles> getRoles() {
        return rolesRepository.findAll();

    }

    @Override
    public ApiResponse getRoleById(Long roleId) {
        Optional<Roles> roles = this.rolesRepository.findById(roleId);
        RolesResponseDto response = modelMapper.map(roles, RolesResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(200)
                .data(response)
                .message("Profile fetched successfully")
                .build();
    }
}
