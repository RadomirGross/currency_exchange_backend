package com.gross.currency_exchange_backend.mapper;

import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;
import com.gross.currency_exchange_backend.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateDTO toDto(ExchangeRate exchangeRate);
    ExchangeRate toEntity(ExchangeRateDTO exchangeRateDTO);
    List<ExchangeRateDTO> toDtoList(List<ExchangeRate> exchangeRates);
}
