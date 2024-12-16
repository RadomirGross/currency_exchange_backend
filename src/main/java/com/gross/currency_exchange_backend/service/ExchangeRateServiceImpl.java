package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.dao.CurrencyDAOImpl;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAO;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange_backend.dto.CurrencyDTO;
import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.mapper.ExchangeRateMapper;
import com.gross.currency_exchange_backend.mapper.ExchangeRateMapperImpl;
import com.gross.currency_exchange_backend.model.Currency;
import com.gross.currency_exchange_backend.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDAO exchangeRateDAO;
    private final ExchangeRateMapper mapper;
    private final CurrencyService currencyService;

    public ExchangeRateServiceImpl(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
        // this.mapper = new ExchangeRateMapperImpl();
        mapper = ExchangeRateMapperImpl.INSTANCE;
        currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
    }

    @Override
    public List<ExchangeRateDTO> getAllExchangeRates() {
        try {
            return mapper.toDtoList(exchangeRateDAO.getExchangeAllRates());

        } catch (SQLException e) {
            throw new CustomServiceException("Ошибка при получении списка курсов валют", 500);
        }
    }

    @Override
    public ExchangeRateDTO getExchangeRate(String path) {
        int[] ids = validateCurrencyCodes(path);
        int baseId = ids[0];
        int targetId = ids[1];
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        try {
            exchangeRateDTO=mapper.toDto(exchangeRateDAO.getExchangeRate(baseId, targetId));
        } catch (SQLException e) {
            throw new CustomServiceException("Ошибка при получении курса валют " + path, 500);
        }
        if(exchangeRateDTO==null)
            throw new CustomServiceException("Для валютной пары "+path+" не найдено курса обмена", 404);
        return exchangeRateDTO;
    }

    @Override
    public ExchangeRateDTO addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate)  {
        int baseCurrencyId = validateCurrencyCode(baseCurrencyCode);
        int targetCurrencyId = validateCurrencyCode(targetCurrencyCode);
        validateRate(rate);
        try {return mapper.toDto(exchangeRateDAO.addExchangeRate(baseCurrencyId,targetCurrencyId,rate));}
        catch (SQLException e) {
            throw new CustomServiceException("Ошибка при добавлении курса валют ", 500);
        }
    }

    @Override
    public ExchangeRateDTO updateExchangeRate(String path, BigDecimal rate) {
        validateCurrencyCodes(path);
        validateRate(rate);
        ExchangeRateDTO updatedExchangeRateDTO = getExchangeRate(path);
        try {
                exchangeRateDAO.updateExchangeRate(updatedExchangeRateDTO.getId(), rate);
                updatedExchangeRateDTO.setRate(rate);
                return updatedExchangeRateDTO;
        }
        catch (SQLException e) {
            throw new CustomServiceException("Ошибка при обновлении курса валют ", 500);
        }
    }

    public int[] validateCurrencyCodes(String path) {
        if (path.length() != 6)
            throw new CustomServiceException("Длина курсов валют должна быть 6 символов," +
                    "например: 'exchangeRate/USDCNY'", 400);
        String baseCurrencyCode = path.substring(0, 3);
        String targetCurrencyCode = path.substring(3);
        int baseCurrencyId = validateCurrencyCode(baseCurrencyCode);
        int targetCurrencyId = validateCurrencyCode(targetCurrencyCode);
        return new int[]{baseCurrencyId, targetCurrencyId};
    }

    public int validateCurrencyCode(String code) {
        CurrencyDTO currency = currencyService.getCurrencyByCode(code);
        if (currency == null) {
            throw new CustomServiceException("Валюты с кодом " + code + " не найдено", 404);
        }
        return currency.getId();
    }

    public void validateRate(BigDecimal rate)
    {
        if (rate==null)
            throw new CustomServiceException("Rate не может быть равным 0",409);
        if (rate.compareTo(BigDecimal.ZERO) == 0)
            throw new CustomServiceException("Rate не может быть равным 0",409);
        if (rate.compareTo(BigDecimal.ZERO) < 0)
            throw new CustomServiceException("Rate не может быть меньше 0",409);
    }
}
