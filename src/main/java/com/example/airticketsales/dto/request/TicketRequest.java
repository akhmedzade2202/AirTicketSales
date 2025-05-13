package com.example.airticketsales.dto.request;

import com.example.airticketsales.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotNull(message = "Uçuş ID-si boş ola bilməz")
    private Long flightId;

    @NotBlank(message = "Sərnişinin adı boş ola bilməz")
    private String passengerName;

    @NotBlank(message = "Sərnişinin soyadı boş ola bilməz")
    private String passengerSurname;

    @NotBlank(message = "Pasport nömrəsi boş ola bilməz")
    private String passportNumber;

    @NotNull(message = "Oturacaq tipi boş ola bilməz")
    private SeatType seatType;

    @NotBlank(message = "Oturacaq nömrəsi boş ola bilməz")
    private String seatNumber;
}
