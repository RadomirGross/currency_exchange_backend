package com.gross.currency_exchange_backend.dao;


import com.gross.currency_exchange_backend.model.Currency;
import com.gross.currency_exchange_backend.utils.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    @Override
    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String sql = "select * from \"Currencies\"";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                currencies.add(currency);
            }
        }
        return currencies;
    }

    @Override
    public Currency getCurrencyByCode(String code) throws SQLException {
        Currency currency = new Currency();
        String sql = "select * from \"Currencies\" where \"Code\" = ?";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    currency.setId(resultSet.getInt("ID"));
                    currency.setCode(resultSet.getString("Code"));
                    currency.setName(resultSet.getString("FullName"));
                    currency.setSign(resultSet.getString("Sign"));
                    return currency;
                }
            }
        }
        return null;
    }

    @Override
    public Currency addCurrency(Currency currency) throws SQLException {

        String sql = "INSERT INTO \"Currencies\" (\"Code\",\"FullName\", \"Sign\") VALUES (?, ?, ?)";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();
            System.out.println("Currency added successfully.");
            return getCurrencyByCode(currency.getCode());

        }

    }

    @Override
    public Currency getCurrencyById(int id) throws SQLException {
        Currency currency = new Currency();
        String sql = "select * from \"Currencies\" where \"ID\" = ?";

        try (Connection connection = SessionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    currency.setId(resultSet.getInt("ID"));
                    currency.setCode(resultSet.getString("Code"));
                    currency.setName(resultSet.getString("FullName"));
                    currency.setSign(resultSet.getString("Sign"));
                }
            }
            return currency;
        }

    }
}

