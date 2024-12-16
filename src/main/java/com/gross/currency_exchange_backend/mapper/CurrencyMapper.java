package com.gross.currency_exchange_backend.mapper;

import com.gross.currency_exchange_backend.dto.CurrencyDTO;
import com.gross.currency_exchange_backend.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface CurrencyMapper {
CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

CurrencyDTO toDto(Currency currency);
Currency toEntity(CurrencyDTO dto);
List<CurrencyDTO> toDtoList(List<Currency> currencies);
}
