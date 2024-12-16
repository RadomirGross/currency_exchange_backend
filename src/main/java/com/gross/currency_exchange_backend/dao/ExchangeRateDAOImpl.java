package com.gross.currency_exchange_backend.dao;

import com.gross.currency_exchange_backend.model.ExchangeRate;
import com.gross.currency_exchange_backend.utils.SessionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    @Override
    public List<ExchangeRate> getExchangeAllRates() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "select * from \"ExchangeRates\"";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getInt("ID"));
                exchangeRate.setBaseCurrency(currencyDAO.getCurrencyById
                        (resultSet.getInt("BaseCurrencyId")));
                exchangeRate.setTargetCurrency(currencyDAO.getCurrencyById
                        (resultSet.getInt("TargetCurrencyId")));
                exchangeRate.setRate(resultSet.getBigDecimal("Rate"));
                exchangeRates.add(exchangeRate);
            }
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate getExchangeRate(int baseCurrencyId, int targetCurrencyId) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        String sql = "select * from \"ExchangeRates\" where \"BaseCurrencyId\" = ? and \"TargetCurrencyId\" = ?";
        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate.setId(resultSet.getInt("ID"));
                    exchangeRate.setBaseCurrency(currencyDAO.getCurrencyById(baseCurrencyId));  // Получаем валюту по ID
                    exchangeRate.setTargetCurrency(currencyDAO.getCurrencyById(targetCurrencyId));  // Получаем валюту по ID
                    exchangeRate.setRate(resultSet.getBigDecimal("Rate"));
                    return exchangeRate;
                } else {
                    return null;
                }
            }
        }

    }



    @Override
    public ExchangeRate addExchangeRate(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) throws SQLException {
        String sql = "INSERT INTO \"ExchangeRates\" (\"BaseCurrencyId\", \"TargetCurrencyId\", \"Rate\") VALUES (?, ?, ?)";
        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            preparedStatement.setBigDecimal(3, rate);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exchange rate added successfully");
                return getExchangeRate(baseCurrencyId, targetCurrencyId);
            } else return null;
        }

    }

    @Override
    public void updateExchangeRate(int id, BigDecimal newRate) throws SQLException {
        String sql = "UPDATE \"ExchangeRates\" SET \"Rate\" = ? WHERE \"ID\" = ?";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, newRate);
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exchange rate updated successfully");
            } else
                System.out.println("Exchange rate not updated");
        }
    }
}
