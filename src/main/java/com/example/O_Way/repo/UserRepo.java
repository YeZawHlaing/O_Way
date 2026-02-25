package com.example.O_Way.repo;

import com.example.O_Way.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    User findByName(String name);

    boolean existsByName(String name);
    List<User> findByRoles_Name(String roleName);
    @Transactional
    void deleteByVehicleId(Long vehicleId);
}