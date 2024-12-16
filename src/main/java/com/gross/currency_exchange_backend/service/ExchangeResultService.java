package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.model.ExchangeResult;

import java.math.BigDecimal;

public interface ExchangeResultService {
    ExchangeResult getExchangeResult(String baseCurrency, String targetCurrency, BigDecimal amount);
}
