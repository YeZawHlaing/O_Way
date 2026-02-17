package com.example.O_Way.repo;

import com.example.O_Way.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ProfileRepo extends JpaRepository<Profile,Long> {
    Optional<Profile> findByUser_Id(Long userId);
    Optional<Profile> findByUserName(String name);


}
