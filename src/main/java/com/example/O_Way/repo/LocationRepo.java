package com.example.O_Way.repo;

import com.example.O_Way.model.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends JpaRepository<Location,Long> {
    @Transactional
    void deleteByVehicleId(Long vehicleId);
}
