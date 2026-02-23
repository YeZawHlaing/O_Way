package com.example.O_Way.repo;

import com.example.O_Way.model.Payment;
import com.example.O_Way.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {
    Optional<Payment> findByRental(Rental rental);

}
