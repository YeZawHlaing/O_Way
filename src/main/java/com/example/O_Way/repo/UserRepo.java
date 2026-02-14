package com.example.O_Way.repo;

import com.example.O_Way.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    User findByName(String name);

    boolean existsByName(String name);
}