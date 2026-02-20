package com.example.O_Way.repo;

import com.example.O_Way.model.Rental;
import com.example.O_Way.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepo extends JpaRepository<Rental,Long> {

    Optional<Rental> findByIdAndCustomer_Name(Long id, String name);
    List<Rental> findByVehicleUserName(String name);
    List<Rental> findByVehicleId(Long vehicleId);
    List<Rental> findByCustomer(User customer);

    @Query("SELECT r FROM Rental r JOIN FETCH r.customer WHERE r.vehicle.id = :vehicleId")
    List<Rental> findByVehicleIdWithCustomer(@Param("vehicleId") Long vehicleId);
}
