package com.example.O_Way.service;

import com.example.O_Way.dto.responseDto.WalletResponseDto;
import com.example.O_Way.model.Wallet;

public interface WalletService {

    Wallet createWalletForCurrentUser();
    WalletResponseDto getMyWallet();

}
