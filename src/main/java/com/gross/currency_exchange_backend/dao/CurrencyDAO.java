package com.gross.currency_exchange_backend.dao;

import com.gross.currency_exchange_backend.model.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;
    Currency getCurrencyByCode(String code) throws SQLException;
    Currency getCurrencyById(int id) throws SQLException;
    Currency addCurrency(Currency currency) throws SQLException;
}
