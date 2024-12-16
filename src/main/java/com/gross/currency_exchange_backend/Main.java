package com.gross.currency_exchange_backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange_backend.dao.CurrencyDAO;
import com.gross.currency_exchange_backend.dao.CurrencyDAOImpl;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAO;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange_backend.model.Currency;
import com.gross.currency_exchange_backend.service.ExchangeRateService;
import com.gross.currency_exchange_backend.service.ExchangeRateServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, JsonProcessingException {
        CurrencyDAO currencyDAO = new CurrencyDAOImpl();
        ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAOImpl();
        ObjectMapper objectMapper = new ObjectMapper();
      // List<Currency> currencies=currencyDAO.getAllCurrencies();
     //   System.out.println(objectMapper.writeValueAsString(currencies));
        //Currency CAD=currencyDAO.getCurrencyByCode("AAA");
      //  System.out.println(objectMapper.writeValueAsString(CAD));
     //   Currency USD=new Currency("BBB", "Dollar","$");
     //   System.out.println(currencyDAO.addCurrency(USD));
      /*  Currency currency=currencyDAO.getCurrencyById(1);
        System.out.println(currency);
        List<ExchangeRate> exchangeRates=exchangeRateDAO.getExchangeRates();*/
        // System.out.println(objectMapper.writeValueAsString(exchangeRates));
      /*  ExchangeRate exchangeRate=exchangeRateDAO.getExchangeRate(1,2);
        System.out.println(objectMapper.writeValueAsString(exchangeRate));*/
        //ExchangeRate exchangeRate = exchangeRateDAO.addExchangeRate(9, 5, BigDecimal.valueOf(0.12));
       // System.out.println(objectMapper.writeValueAsString(exchangeRate));
       // exchangeRateDAO.updateExchangeRate(2, BigDecimal.valueOf(555));
        ExchangeRateService exchangeRateService=new ExchangeRateServiceImpl(new ExchangeRateDAOImpl());
       // System.out.println(objectMapper.writeValueAsString(exchangeRateService.getAllExchangeRates()));
        System.out.println(objectMapper.writeValueAsString(exchangeRateService.getExchangeRate("CADJPY")));
    }
}
