package com.gross.currency_exchange_backend.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange_backend.dao.CurrencyDAOImpl;
import com.gross.currency_exchange_backend.dto.CurrencyDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.service.CurrencyService;
import com.gross.currency_exchange_backend.service.CurrencyServiceImpl;
import com.gross.currency_exchange_backend.utils.ErrorHandler;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class SingleCurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ObjectMapper jsonMapper;

    public SingleCurrencyServlet() {
        currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        String code = path.substring(1).toUpperCase();
        try {
            CurrencyDTO currencyDTO = currencyService.getCurrencyByCode(code);
            if (currencyDTO != null) {
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(currencyDTO));}
            else{
                throw new CustomServiceException("Валюты с кодом "+code+" не найдено",404);
            }
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }


    }
}
