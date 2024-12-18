package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.dao.CurrencyDAO;
import com.gross.currency_exchange_backend.dto.CurrencyDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.mapper.CurrencyMapper;
import com.gross.currency_exchange_backend.model.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDAO currencyDAO;
    private final CurrencyMapper currencyMapper;
    private final int CODE_LENGTH = 3;
    private final int SIGN_LENGTH = 3;
    private final int NAME_LENGTH = 30;

    public CurrencyServiceImpl(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
        currencyMapper = CurrencyMapper.INSTANCE;
    }

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        try {
            return currencyMapper.toDtoList(currencyDAO.getAllCurrencies());
        } catch (SQLException e) {
            throw new CustomServiceException("Ошибка при получении списка валют", 500);
        }
    }

    @Override
    public CurrencyDTO getCurrencyByCode(String code) {
        validateCurrencyCode(code);
        try {
            Currency currency=currencyDAO.getCurrencyByCode(code);
            if(currency!=null){
                return currencyMapper.toDto(currency);
            }else return null;
        } catch (SQLException e) {
            throw new CustomServiceException("Ошибка при получении валюты", 500);
        }
    }

    @Override
    public CurrencyDTO addCurrency(String code, String name, String sign) {
        validateCurrencyCode(code);
        validateParameter(sign, SIGN_LENGTH, "обозначения валюты");
        validateParameter(name, NAME_LENGTH, "названия валюты");
        if (getCurrencyByCode(code) != null) {
            throw new CustomServiceException("Валюта с кодом "+code+" уже существует",409);
        }
        Currency currency = new Currency(code, name, sign);
        try {
            return currencyMapper.toDto(currencyDAO.addCurrency(currency));
        } catch (SQLException e) {
            throw new CustomServiceException("Ошибка при добавлении валюты", 500);

        }
    }

    public void validateCurrencyCode(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new CustomServiceException("Длина кода валют должна быть 3 символа, например: 'USD'", 400);
        }
    }

    public void validateParameter(String param, int length, String parameterName) {
        if (param == null || param.isEmpty())
            throw new CustomServiceException(parameterName + " не должен(-o) быть null или Empty", 400);
        if (param.length() > length)
            throw new CustomServiceException("Длина "+parameterName+" не должна превышать " + length + " символа(-ов)", 400);
    }
}
