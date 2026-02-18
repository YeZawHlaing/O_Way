package com.example.O_Way.repo;

import com.example.O_Way.model.O_Way_Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface O_Way_PayRepo extends JpaRepository<O_Way_Pay,Long> {
}
