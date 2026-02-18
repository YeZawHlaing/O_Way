package com.example.O_Way.repo;

import com.example.O_Way.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    // Filter by user name and date range
    List<Transaction> findByWallet_User_NameAndCreatedAtBetween(
            String name, LocalDateTime from, LocalDateTime to
    );

    // All transactions for a user
    List<Transaction> findByWallet_User_Name(String name);
}
