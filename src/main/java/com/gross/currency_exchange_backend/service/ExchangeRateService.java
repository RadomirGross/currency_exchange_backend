package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRateDTO> getAllExchangeRates();
    ExchangeRateDTO getExchangeRate(String path);
    ExchangeRateDTO addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
    ExchangeRateDTO updateExchangeRate(String path, BigDecimal rate);
}
