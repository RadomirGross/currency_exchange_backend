package com.gross.currency_exchange_backend.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.mapper.ExchangeRateMapper;
import com.gross.currency_exchange_backend.service.ExchangeRateService;
import com.gross.currency_exchange_backend.service.ExchangeRateServiceImpl;
import com.gross.currency_exchange_backend.utils.ErrorHandler;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper jsonMapper;
    public final ExchangeRateMapper mapper;

    public ExchangeRateServlet()  {
        exchangeRateService=new ExchangeRateServiceImpl(new ExchangeRateDAOImpl());
        jsonMapper=new ObjectMapper();
        mapper=ExchangeRateMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        try{
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(exchangeRateService.getAllExchangeRates()));
        }catch (CustomServiceException e)
        {ErrorHandler.sendError(response,e.getErrorCode(),e.getMessage());}
        catch (Exception e)
        {ErrorHandler.sendError(response, 500, "Ошибка на сервере");}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
       try{
           String baseCurrencyCode=request.getParameter("baseCurrencyCode");
           String targetCurrencyCode=request.getParameter("targetCurrencyCode");
           BigDecimal rate=new BigDecimal(request.getParameter("rate"));
           ExchangeRateDTO addedExchangeRate=exchangeRateService
                   .addExchangeRate(baseCurrencyCode,targetCurrencyCode,rate);
           if(addedExchangeRate!=null)
           {response.setStatus(201);
            response.getWriter().write(jsonMapper.writeValueAsString(addedExchangeRate));}
       } catch (CustomServiceException e)
           {ErrorHandler.sendError(response,e.getErrorCode(),e.getMessage());}
       catch (Exception e)
       {ErrorHandler.sendError(response, 500, "Ошибка на сервере");}
    }
}
