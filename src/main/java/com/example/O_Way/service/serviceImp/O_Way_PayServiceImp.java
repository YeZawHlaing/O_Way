package com.example.O_Way.service.serviceImp;

import com.example.O_Way.dto.requestDto.O_Way_PayRequestDto;
import com.example.O_Way.dto.responseDto.O_Way_PayResponseDto;
import com.example.O_Way.model.O_Way_Pay;
import com.example.O_Way.model.Payment;
import com.example.O_Way.model.Transfer;
import com.example.O_Way.model.User;
import com.example.O_Way.model.Wallet;
import com.example.O_Way.repo.*;
import com.example.O_Way.util.status.PaymentStatus;
import com.example.O_Way.util.status.TransferStatus;
import com.example.O_Way.util.status.Type;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class O_Way_PayServiceImp implements com.example.O_Way.service.O_Way_PayService {

    private final O_Way_PayRepo oWayPayRepo;
    private final PaymentRepo paymentRepo;
    private final WalletRepo walletRepo;
    private final TransferRepo transferRepo;
    private final UserRepo userRepository;

    @Override
    @Transactional
    public O_Way_PayResponseDto pay(O_Way_PayRequestDto request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByName(username);

        Payment payment = paymentRepo.findById(request.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // Ensure user owns this payment
        if (!payment.getRental().getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to pay this rental");
        }

        // Check if already paid
        if (payment.getStatus().name().equals("SUCCESS")) {
            throw new RuntimeException("Payment already completed");
        }

        // 🔹 Create Transfer from user's wallet to agent's wallet
        Wallet fromWallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        Wallet toWallet = walletRepo.findById(1L) // Example: Agent wallet, adjust as needed
                .orElseThrow(() -> new EntityNotFoundException("Agent wallet not found"));

        if (fromWallet.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct and credit
        fromWallet.setBalance(fromWallet.getBalance().subtract(payment.getAmount()));
        toWallet.setBalance(toWallet.getBalance().add(payment.getAmount()));
        walletRepo.save(fromWallet);
        walletRepo.save(toWallet);

        // Create Transfer
        Transfer transfer = new Transfer();
        transfer.setFromWallet(fromWallet);
        transfer.setToWallet(toWallet);
        transfer.setAmount(payment.getAmount());
        transfer.setTransferStatus(TransferStatus.COMPLETED);
        transfer.setType(Type.TRANSFER);
        transfer.setCreatedAt(LocalDateTime.now());

        Transfer savedTransfer = transferRepo.save(transfer);

        // Update payment
        payment.setStatus(PaymentStatus.PAID);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

        // Create O_Way_Pay record
        O_Way_Pay oWayPay = new O_Way_Pay();
        oWayPay.setPayment(payment);
        oWayPay.setTransfer(savedTransfer);

        O_Way_Pay savedOwayPay = oWayPayRepo.save(oWayPay);

        return new O_Way_PayResponseDto(
                savedOwayPay.getId(),
                payment.getId(),
                savedTransfer.getId()
        );
    }
}