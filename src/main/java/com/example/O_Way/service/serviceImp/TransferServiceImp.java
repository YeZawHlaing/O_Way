package com.example.O_Way.service.serviceImp;

import com.example.O_Way.dto.requestDto.TransferRequestDto;
import com.example.O_Way.dto.responseDto.TransferResponseDto;
import com.example.O_Way.model.*;
import com.example.O_Way.repo.*;
import com.example.O_Way.service.TransferService;
import com.example.O_Way.util.status.*;
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
    private final RentalRepo rentalRepo;
    private final O_Way_PayRepo oWayPayRepo;
    private final PaymentRepo paymentRepo;

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

    @Override
    @Transactional
    public TransferResponseDto transferForRental(Long rentalId) {

        // 1️⃣ Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User authenticatedUser = userRepository.findByName(username);


        // 2️⃣ Get rental
        Rental rental = rentalRepo.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

//         3️⃣ Validate customer ownership by ID
        if (!rental.getCustomer().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Unauthorized rental payment attempt");
        }

        // 4️⃣ Get payment for this rental
        Payment payment = paymentRepo.findByRentalId(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for this rental"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Payment already completed");
        }

        // 5️⃣ Get wallets
        Wallet fromWallet = walletRepo.findByUser(rental.getCustomer())
                .orElseThrow(() -> new EntityNotFoundException("Customer wallet not found"));

        Wallet toWallet = walletRepo.findByUser(rental.getDriver())
                .orElseThrow(() -> new EntityNotFoundException("Driver wallet not found"));

        BigDecimal amount = payment.getAmount();

        // 6️⃣ Validate balance
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 7️⃣ Update balances
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepo.save(fromWallet);
        walletRepo.save(toWallet);

        // 8️⃣ Create Transfer
        Transfer transfer = new Transfer();
        transfer.setFromWallet(fromWallet);
        transfer.setToWallet(toWallet);
        transfer.setAmount(amount);
        transfer.setTransferStatus(TransferStatus.COMPLETED);
        transfer.setType(Type.TRANSFER);
        transfer.setCreatedAt(LocalDateTime.now());

        Transfer savedTransfer = transferRepo.save(transfer);

        // 9️⃣ Create Ledger Transactions
        // DEBIT
        Transaction debit = new Transaction();
        debit.setWallet(fromWallet);
        debit.setAmount(amount);
        debit.setDirection(Direction.DEBIT);
        debit.setType(Type.TRANSFER);
        debit.setTransactionStatus(TransactionStatus.SUCCESS);
        debit.setTransfer(savedTransfer);
        debit.setReferenceId("PAY-" + payment.getId());
        debit.setCreatedAt(LocalDateTime.now());
        transactionRepo.save(debit);

        // CREDIT
        Transaction credit = new Transaction();
        credit.setWallet(toWallet);
        credit.setAmount(amount);
        credit.setDirection(Direction.CREDIT);
        credit.setType(Type.TRANSFER);
        credit.setTransactionStatus(TransactionStatus.SUCCESS);
        credit.setTransfer(savedTransfer);
        credit.setReferenceId("PAY-" + payment.getId());
        credit.setCreatedAt(LocalDateTime.now());
        transactionRepo.save(credit);

        // 🔟 Update Payment
        payment.setStatus(PaymentStatus.PAID);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepo.save(payment);

//         1️⃣1️⃣ Update Rental
        rental.setPaid_at(LocalDateTime.now());
        rental.setRentalStatus(Rental_Status.PAID);
        rentalRepo.save(rental);

        // 1️⃣2️⃣ Create O_Way_Pay link
        O_Way_Pay link = new O_Way_Pay();
        link.setPayment(payment);
        link.setTransfer(savedTransfer);
        oWayPayRepo.save(link);

        // 1️⃣3️⃣ Return response
        return new TransferResponseDto(
                savedTransfer.getId(),
                fromWallet.getId(),
                toWallet.getId(),
                amount,
                savedTransfer.getTransferStatus(),
                savedTransfer.getType(),
                savedTransfer.getCreatedAt()
        );
    }
}