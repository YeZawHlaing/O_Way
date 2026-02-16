package com.example.O_Way.controller;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.model.Roles;
import com.example.O_Way.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RolesController {

    private final RolesService rolesService;

    @PostMapping
    public ResponseEntity<Roles> createRole(@RequestBody Roles role) {

        Roles savedRole = rolesService.CreateRole(role);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/{id}")
    public ApiResponse getRoleById(@PathVariable("id") Long roleId) {
        return rolesService.getRoleById(roleId);
    }

    // 👇 GET ALL ROLES
    @GetMapping("/getRoles")
    public ResponseEntity<List<Roles>> getRoles() {
        return ResponseEntity.ok(rolesService.getRoles());
    }
}