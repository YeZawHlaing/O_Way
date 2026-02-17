package com.example.O_Way.service.serviceImp;

import com.example.O_Way.dto.responseDto.UserResponseDto;
import com.example.O_Way.dto.responseDto.WalletResponseDto;
import com.example.O_Way.model.User;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.WalletRepo;
import com.example.O_Way.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImp implements WalletService {

    private final WalletRepo walletRepository;
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Wallet createWalletForCurrentUser() {

        // 1️⃣ Get authenticated username
        String name = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // 2️⃣ Find user
        User user = userRepository.findByName(name);

        // 3️⃣ Check if wallet already exists
        if (walletRepository.existsByUserName(name)) {
            throw new RuntimeException("Wallet already exists");
        }

        // 4️⃣ Create wallet
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }


    @Override
    public WalletResponseDto getMyWallet() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username);

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        WalletResponseDto response =
                modelMapper.map(wallet, WalletResponseDto.class);

        return response;
    }
}
