package com.gross.currency_exchange_backend.dao;

import com.gross.currency_exchange_backend.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {
    List<ExchangeRate> getExchangeAllRates() throws SQLException;
    ExchangeRate getExchangeRate(int baseCurrencyId, int targetCurrencyId) throws SQLException;
    ExchangeRate addExchangeRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) throws SQLException;
    void updateExchangeRate(int id, BigDecimal newRate) throws SQLException;
}
