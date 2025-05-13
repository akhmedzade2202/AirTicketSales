package com.example.airticketsales.dto.response;

import com.example.airticketsales.entity.Payment;
import com.example.airticketsales.enums.PaymentStatus;
import com.example.airticketsales.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Long id;
    private FlightResponse flight;
    private String passengerName;
    private String passengerSurname;
    private String passportNumber;
    private SeatType seatType;
    private String seatNumber;
    private LocalDateTime creationDate;
    private BigDecimal totalPrice;
    private PaymentStatus paymentStatus;

}
