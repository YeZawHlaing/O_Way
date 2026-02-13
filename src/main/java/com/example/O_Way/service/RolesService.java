package com.example.O_Way.service;

import com.example.O_Way.model.Roles;

import java.util.List;

public interface RolesService {

    public Roles CreateRole(Roles role);
    List<Roles> getRoles();

}
