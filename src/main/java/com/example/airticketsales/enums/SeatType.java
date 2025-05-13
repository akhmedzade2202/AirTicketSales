package com.example.airticketsales.enums;

import java.math.BigDecimal;

public enum SeatType {
    REGULAR("SADE", BigDecimal.valueOf(1)),
    VIP("VIP", BigDecimal.valueOf(1.5)),
    BUSINESS("BIZNES", BigDecimal.valueOf(2));

    private final String displayName;
    private final BigDecimal priceMultiplier;

    SeatType(String displayName, BigDecimal priceMultiplier) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getPriceMultiplier() {
        return priceMultiplier;
    }
}

