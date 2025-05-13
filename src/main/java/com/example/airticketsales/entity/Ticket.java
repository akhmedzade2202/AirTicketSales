package com.example.airticketsales.entity;


import com.example.airticketsales.enums.SeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private String passengerSurname;

    @Column(nullable = false)
    private String passportNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Payment payment;

    // Bilet qiymətini hesablayan metod
    @PrePersist
    @PreUpdate
    public void calculatePrice() {
        if (flight != null && seatType != null) {
            this.totalPrice = flight.getPrice().multiply(seatType.getPriceMultiplier());
        }
    }
}
