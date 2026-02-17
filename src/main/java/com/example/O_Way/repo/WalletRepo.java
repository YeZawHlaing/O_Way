package com.example.O_Way.repo;

import com.example.O_Way.model.User;
import com.example.O_Way.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepo extends JpaRepository<Wallet,Long> {

    Optional<Wallet> findByUserName(String name);

    boolean existsByUserName(String name);
    Optional<Wallet> findByUser(User user);


}
