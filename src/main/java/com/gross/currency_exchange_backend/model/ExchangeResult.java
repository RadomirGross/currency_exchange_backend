package com.gross.currency_exchange_backend.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeResult {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
