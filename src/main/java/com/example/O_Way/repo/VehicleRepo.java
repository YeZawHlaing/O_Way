package com.example.O_Way.repo;

import com.example.O_Way.model.User;
import com.example.O_Way.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle,Long> {
    Optional<Vehicle> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);
}
