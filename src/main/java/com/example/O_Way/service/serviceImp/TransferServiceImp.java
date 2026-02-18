package com.example.O_Way.service.serviceImp;

import com.example.O_Way.dto.requestDto.TransferRequestDto;
import com.example.O_Way.dto.responseDto.TransferResponseDto;
import com.example.O_Way.model.Transaction;
import com.example.O_Way.model.Transfer;
import com.example.O_Way.model.User;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.repo.TransactionRepo;
import com.example.O_Way.repo.TransferRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.repo.WalletRepo;
import com.example.O_Way.service.TransferService;
import com.example.O_Way.util.status.Direction;
import com.example.O_Way.util.status.TransactionStatus;
import com.example.O_Way.util.status.TransferStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferServiceImp implements TransferService {

    private final WalletRepo walletRepo;
    private final TransferRepo transferRepo;
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;
    private final TransactionRepo transactionRepo;

    @Override
    @Transactional
    public TransferResponseDto transfer(TransferRequestDto request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username);

        Wallet fromWallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        Wallet toWallet = walletRepo.findById(request.getToWalletId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver wallet not found"));

        if (fromWallet.getId().equals(toWallet.getId())) {
            throw new RuntimeException("Cannot transfer to your own wallet");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💰 Update balances
        fromWallet.setBalance(
                fromWallet.getBalance().subtract(request.getAmount())
        );

        toWallet.setBalance(
                toWallet.getBalance().add(request.getAmount())
        );

        walletRepo.save(fromWallet);
        walletRepo.save(toWallet);

        // 📝 Save Transfer
        Transfer transfer = new Transfer();
        transfer.setFromWallet(fromWallet);
        transfer.setToWallet(toWallet);
        transfer.setAmount(request.getAmount());
        transfer.setTransferStatus(TransferStatus.COMPLETED);
        transfer.setType(request.getType());
        transfer.setCreatedAt(LocalDateTime.now());

        Transfer savedTransfer = transferRepo.save(transfer);

        // ===============================
        // 🔥 CREATE TRANSACTIONS (LEDGER)
        // ===============================

        // 1️⃣ Sender DEBIT
        Transaction debitTransaction = new Transaction();
        debitTransaction.setWallet(fromWallet);
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setDirection(Direction.DEBIT);
        debitTransaction.setType(request.getType());
        debitTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        debitTransaction.setTransfer(savedTransfer);
        debitTransaction.setReferenceId("TRF-" + savedTransfer.getId());
        debitTransaction.setCreatedAt(LocalDateTime.now());

        transactionRepo.save(debitTransaction);

        // 2️⃣ Receiver CREDIT
        Transaction creditTransaction = new Transaction();
        creditTransaction.setWallet(toWallet);
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setDirection(Direction.CREDIT);
        creditTransaction.setType(request.getType());
        creditTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        creditTransaction.setTransfer(savedTransfer);
        creditTransaction.setReferenceId("TRF-" + savedTransfer.getId());
        creditTransaction.setCreatedAt(LocalDateTime.now());

        transactionRepo.save(creditTransaction);

        // ===============================

        return new TransferResponseDto(
                savedTransfer.getId(),
                fromWallet.getId(),
                toWallet.getId(),
                savedTransfer.getAmount(),
                savedTransfer.getTransferStatus(),
                savedTransfer.getType(),
                savedTransfer.getCreatedAt()
        );
    }
}