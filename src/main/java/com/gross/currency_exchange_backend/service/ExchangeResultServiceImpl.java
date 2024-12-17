package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.dao.CurrencyDAOImpl;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.mapper.CurrencyMapper;
import com.gross.currency_exchange_backend.model.Currency;
import com.gross.currency_exchange_backend.model.ExchangeResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExchangeResultServiceImpl implements ExchangeResultService {
    ExchangeRateService exchangeRateService;
    CurrencyService currencyService;
    CurrencyMapper currencyMapper;

    public ExchangeResultServiceImpl() {
        exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl());
        currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        currencyMapper = CurrencyMapper.INSTANCE;
    }

    @Override
    public ExchangeResult getExchangeResult(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        validateAmount(amount);
        ExchangeResult exchangeResult = new ExchangeResult();
        Currency targetCurrency = currencyMapper
                .toEntity(currencyService.getCurrencyByCode(baseCurrencyCode));
        Currency baseCurrency = currencyMapper
                .toEntity(currencyService.getCurrencyByCode(targetCurrencyCode));
        if (baseCurrency != null && targetCurrency != null) {
            exchangeResult.setBaseCurrency(baseCurrency);
            exchangeResult.setTargetCurrency(targetCurrency);
        }
        BigDecimal rate = getDirectRate(baseCurrencyCode, targetCurrencyCode);
        if (rate == null) {
            rate = getReverseRate(baseCurrencyCode, targetCurrencyCode);
        }
        if (rate == null) {
            rate = getRateUsingUSD(baseCurrencyCode, targetCurrencyCode);
        }
        if (rate != null) {
            exchangeResult.setRate(rate);
        } else throw new CustomServiceException("Для валютной пары " + baseCurrencyCode + " " +
                targetCurrencyCode + " не найдено сценариев обмена", 404);
        exchangeResult.setAmount(amount);
        exchangeResult.setConvertedAmount(amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
        return exchangeResult;
    }

    private BigDecimal getDirectRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRateDTO exchangeRate = exchangeRateService
                    .getExchangeRate(baseCurrencyCode + targetCurrencyCode);
            return exchangeRate != null ? exchangeRate.getRate() : null;
        } catch (CustomServiceException e) {
            return null;
        }
    }

    private BigDecimal getReverseRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRateDTO exchangeRate = exchangeRateService
                    .getExchangeRate(targetCurrencyCode + baseCurrencyCode);
            BigDecimal rate = exchangeRate.getRate();
            validateRate(rate);
            return BigDecimal.ONE.divide(rate, MathContext.DECIMAL128);
        } catch (CustomServiceException e) {
            return null;
        }
    }

    private BigDecimal getRateUsingUSD(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRateDTO exchangeRateBaseCurrencyAndUSD = exchangeRateService
                    .getExchangeRate("USD" + baseCurrencyCode);
            ExchangeRateDTO exchangeRateTargetCurrencyAndUSD = exchangeRateService
                    .getExchangeRate("USD" + targetCurrencyCode);
            if (exchangeRateBaseCurrencyAndUSD != null && exchangeRateTargetCurrencyAndUSD != null)
                return exchangeRateBaseCurrencyAndUSD.getRate()
                        .divide(exchangeRateTargetCurrencyAndUSD.getRate(), 6, RoundingMode.HALF_UP);
            else return null;
        } catch (CustomServiceException e) {
            return null;
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0)
            throw new CustomServiceException("Amount не может быть равным 0", 409);
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new CustomServiceException("Amount не может быть меньше 0", 409);
    }

    private void validateRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) == 0)
            throw new CustomServiceException("Rate не может быть равным 0", 409);
        if (rate.compareTo(BigDecimal.ZERO) < 0)
            throw new CustomServiceException("Rate не может быть меньше 0", 409);
    }
}