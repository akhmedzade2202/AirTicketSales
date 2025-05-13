package com.example.airticketsales.service;

import com.example.airticketsales.dto.request.TicketRequest;
import com.example.airticketsales.dto.response.TicketResponse;
import com.example.airticketsales.entity.Flight;
import com.example.airticketsales.entity.Payment;
import com.example.airticketsales.entity.Ticket;
import com.example.airticketsales.entity.User;
import com.example.airticketsales.enums.PaymentStatus;
import com.example.airticketsales.exception.NotFoundException;
import com.example.airticketsales.repository.FlightRepository;
import com.example.airticketsales.repository.PaymentRepository;
import com.example.airticketsales.repository.TicketRepository;
import com.example.airticketsales.repository.UserRepository;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bilet tapılmadı"));
        return modelMapper.map(ticket, TicketResponse.class);
    }

    @org.springframework.transaction.annotation.Transactional
    public TicketResponse createTicket(TicketRequest request, Long userId) {
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException("Uçuş tapılmadı"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("İstifadəçi tapılmadı"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("Bu uçuşda boş yer yoxdur");
        }

        Ticket ticket = new Ticket();
        ticket.setFlight(flight);
        ticket.setUser(user);
        ticket.setPassengerName(request.getPassengerName());
        ticket.setPassengerSurname(request.getPassengerSurname());
        ticket.setPassportNumber(request.getPassportNumber());
        ticket.setSeatType(request.getSeatType());
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setCreationDate(LocalDateTime.now());
        // The calculatePrice method will be automatically called due to @PrePersist

        // Update available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        ticket = ticketRepository.save(ticket);

        return modelMapper.map(ticket, TicketResponse.class);
    }

    @Transactional
    public void cancelTicket(Long id, Long userId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bilet tapılmadı"));

        if (!ticket.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu bilet sizə məxsus deyil");
        }

        Optional<Payment> paymentOpt = paymentRepository.findByTicketId(id);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();

            if (payment.getStatus() == PaymentStatus.CONFIRMED) {
                throw new RuntimeException("Təsdiqlənmiş ödənişi olan bileti ləğv etmək olmaz. Geri qaytarma üçün adminə müraciət edin.");
            } else if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.REJECTED);
                paymentRepository.save(payment);
            }
        }

        // Increment available seats on flight
        Flight flight = ticket.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        ticketRepository.delete(ticket);
    }
}