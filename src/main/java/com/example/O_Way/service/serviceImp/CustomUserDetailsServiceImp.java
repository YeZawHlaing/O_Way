package com.example.O_Way.service.serviceImp;

import com.example.O_Way.model.CustomUserDetails;
import com.example.O_Way.model.User;
import com.example.O_Way.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImp implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userRepo.findByName(name);

        return new CustomUserDetails(user);
    }
}
