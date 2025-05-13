package com.example.airticketsales.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Bilet ID-si boş ola bilməz")
    private Long ticketId;

    @NotBlank(message = "Kart nömrəsi boş ola bilməz")
    @Size(min = 16, max = 16, message = "Kart nömrəsi 16 rəqəmdən ibarət olmalıdır")
    private String cardNumber;
}

