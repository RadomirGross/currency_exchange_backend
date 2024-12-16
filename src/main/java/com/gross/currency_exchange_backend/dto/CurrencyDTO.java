package com.gross.currency_exchange_backend.dto;

import lombok.Data;

@Data
public class CurrencyDTO {
    private int id;
    private String code;
    private String name;
    private String sign;

    public CurrencyDTO(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
