package com.gross.currency_exchange_backend.dto;

import com.gross.currency_exchange_backend.model.Currency;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ExchangeRateDTO {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    BigDecimal rate;
}
