package com.example.airticketsales.service;

import com.example.airticketsales.dto.request.PaymentRequest;
import com.example.airticketsales.dto.response.PaymentResponse;
import com.example.airticketsales.entity.Flight;
import com.example.airticketsales.entity.Payment;
import com.example.airticketsales.entity.Ticket;
import com.example.airticketsales.entity.User;
import com.example.airticketsales.enums.PaymentStatus;
import com.example.airticketsales.enums.UserRole;
import com.example.airticketsales.exception.NotFoundException;
import com.example.airticketsales.repository.FlightRepository;
import com.example.airticketsales.repository.PaymentRepository;
import com.example.airticketsales.repository.TicketRepository;
import com.example.airticketsales.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPendingPayments() {
        return paymentRepository.findByStatus(PaymentStatus.PENDING).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ödəniş tapılmadı"));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByTicketId(Long ticketId) {
        Payment payment = paymentRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new NotFoundException("Bu bilet üçün ödəniş tapılmadı"));
        return mapToResponse(payment);
    }

    @org.springframework.transaction.annotation.Transactional
    public PaymentResponse createPayment(PaymentRequest request, Long userId) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new NotFoundException("Bilet tapılmadı"));

        // Check if the ticket belongs to the user
        if (!ticket.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu bilet sizə məxsus deyil");
        }

        // Check if payment already exists for this ticket
        if (paymentRepository.findByTicketId(ticket.getId()).isPresent()) {
            throw new RuntimeException("Bu bilet üçün ödəniş artıq mövcuddur");
        }

        Payment payment = Payment.builder()
                .ticket(ticket)
                .cardNumber(request.getCardNumber())
                .paymentDate(LocalDateTime.now())
                .status(PaymentStatus.PENDING)
                .amount(ticket.getTotalPrice())
                .build();

        payment = paymentRepository.save(payment);

        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status, User currentUser) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Ödəniş tapılmadı"));

        // Validate that the provided status is one of the allowed values
        if (status != PaymentStatus.PENDING &&
                status != PaymentStatus.CONFIRMED &&
                status != PaymentStatus.REJECTED &&
                status != PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException("Yanlış ödəniş statusu: " + status);
        }

        // Status-specific validations
        if (status == PaymentStatus.CONFIRMED && payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Yalnız gözləmədə olan ödənişləri təsdiqləmək olar");
        }

        if (status == PaymentStatus.REJECTED && payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Yalnız gözləmədə olan ödənişləri rədd etmək olar");
        }

        if (status == PaymentStatus.REFUNDED && payment.getStatus() != PaymentStatus.CONFIRMED) {
            throw new RuntimeException("Yalnız təsdiqlənmiş ödənişləri geri qaytarmaq olar");
        }

        // Update payment status
        payment.setStatus(status);
        payment.setConfirmedBy(currentUser);
        payment.setConfirmationDate(LocalDateTime.now());

        // Handle seat availability changes for rejections and refunds
        if (status == PaymentStatus.REJECTED || status == PaymentStatus.REFUNDED) {
            Flight flight = payment.getTicket().getFlight();
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            flightRepository.save(flight);
        }

        Payment updatedPayment = paymentRepository.save(payment);

        return mapToResponse(updatedPayment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = modelMapper.map(payment, PaymentResponse.class);

        // Mask card number for security (only show last 4 digits)
        String cardNumber = payment.getCardNumber();
        String maskedCardNumber = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
        response.setCardNumber(maskedCardNumber);

        // Map username from confirmedBy object
        if (payment.getConfirmedBy() != null) {
            response.setConfirmedByUsername(payment.getConfirmedBy().getUsername());
        }

        return response;
    }
}