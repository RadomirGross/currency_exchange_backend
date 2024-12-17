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
import java.util.List;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;
    private final ObjectMapper jsonMapper;

    public CurrencyServlet() {
        this.jsonMapper = new ObjectMapper();
        currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<CurrencyDTO> currencies = currencyService.getAllCurrencies();
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(currencies));
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        try {
            CurrencyDTO savedCurrency = currencyService.addCurrency(code, name, sign);
            response.setStatus(201);
            response.getWriter().write(jsonMapper.writeValueAsString(savedCurrency));
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }
    }
}
