package com.gross.currency_exchange_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString

public class Currency {
    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency( String code, String name, String sign)
    {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
