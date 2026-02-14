package com.example.O_Way.service.serviceImp;

import com.example.O_Way.model.Roles;
import com.example.O_Way.repo.RolesRepo;
import com.example.O_Way.service.RolesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImp implements RolesService {

    private final RolesRepo rolesRepository;

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
}
