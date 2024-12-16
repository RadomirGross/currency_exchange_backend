package com.gross.currency_exchange_backend.service;

import com.gross.currency_exchange_backend.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {
    List<CurrencyDTO> getAllCurrencies();
    CurrencyDTO getCurrencyByCode(String code);
    CurrencyDTO addCurrency(String code,String name,String sign);

}
