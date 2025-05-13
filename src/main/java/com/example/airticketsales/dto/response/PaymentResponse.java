package com.example.airticketsales.dto.response;

import com.example.airticketsales.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long ticketId;
    private String cardNumber; // Masked for security
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private LocalDateTime confirmationDate;
    private String confirmedByUsername;
}