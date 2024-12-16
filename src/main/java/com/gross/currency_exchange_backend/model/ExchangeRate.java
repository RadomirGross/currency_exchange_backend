package com.gross.currency_exchange_backend.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@ToString

public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    BigDecimal rate;
}
