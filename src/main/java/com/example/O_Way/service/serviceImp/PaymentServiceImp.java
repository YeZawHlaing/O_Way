package com.example.O_Way.service.serviceImp;

import com.example.O_Way.common.response.ApiResponse;
import com.example.O_Way.dto.requestDto.PaymentPatchRequestDto;
import com.example.O_Way.dto.requestDto.PaymentRequestDto;
import com.example.O_Way.dto.responseDto.PaymentResponseDto;
import com.example.O_Way.dto.responseDto.RentalResponseDto;
import com.example.O_Way.model.Payment;
import com.example.O_Way.model.Rental;
import com.example.O_Way.model.User;
import com.example.O_Way.repo.PaymentRepo;
import com.example.O_Way.repo.RentalRepo;
import com.example.O_Way.repo.UserRepo;
import com.example.O_Way.service.PaymentService;
import com.example.O_Way.util.status.PaymentStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final RentalRepo rentalRepo;
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username);

        // Find rental
        Rental rental = rentalRepo.findById(request.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        // Ensure rental belongs to authenticated user
        if (!rental.getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot pay for this rental");
        }

        // Create new payment
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        // Save payment
        Payment saved = paymentRepo.save(payment);

        // Map to response DTO
        PaymentResponseDto responseDto = modelMapper.map(saved, PaymentResponseDto.class);
        responseDto.setCreatedAt(saved.getCreatedAt());
        responseDto.setUpdatedAt(saved.getUpdatedAt());
        responseDto.setAmount(saved.getAmount());

        // Fetch driver's wallet from rental
        if (rental.getDriver() != null && rental.getDriver().getWallet() != null) {
            responseDto.setDriverWalletId(rental.getDriver().getWallet().getId());
        } else {
            responseDto.setDriverWalletId(null); // or throw exception if required
        }

        return responseDto;
    }

    @Override
    public PaymentResponseDto updatePayment(Long paymentId,
                                            PaymentPatchRequestDto request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByName(username);


        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        // 🔐 Ensure user owns this rental
        if (!payment.getRental().getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // PATCH logic
        if (request.getMethod() != null) {
            payment.setMethod(request.getMethod());
        }

        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }

        payment.setUpdatedAt(LocalDateTime.now());

        Payment updated = paymentRepo.save(payment);

        return mapToDto(updated);
    }

    private PaymentResponseDto mapToDto(Payment payment) {

        PaymentResponseDto dto =
                modelMapper.map(payment, PaymentResponseDto.class);

        dto.setRentalId(payment.getRental().getId());

        return dto;
    }
}