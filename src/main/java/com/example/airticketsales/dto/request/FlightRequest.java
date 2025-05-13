package com.example.airticketsales.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {

    @NotBlank(message = "Uçuş nömrəsi boş ola bilməz")
    private String flightNumber;

    @NotBlank(message = "Çıxış şəhəri boş ola bilməz")
    private String departureCity;

    @NotBlank(message = "Eniş şəhəri boş ola bilməz")
    private String arrivalCity;

    @NotNull(message = "Yola düşmə tarixi boş ola bilməz")
    @Future(message = "Yola düşmə tarixi gələcək tarixdə olmalıdır")
    private LocalDateTime departureDate;


    @NotNull(message = "Qiymət boş ola bilməz")
    @Min(value = 0, message = "Qiymət mənfi ola bilməz")
    private BigDecimal price;

    @NotNull(message = "Mövcud oturacaq sayı boş ola bilməz")
    @Min(value = 1, message = "Ən azı bir oturacaq olmalıdır")
    private Integer availableSeats;
}