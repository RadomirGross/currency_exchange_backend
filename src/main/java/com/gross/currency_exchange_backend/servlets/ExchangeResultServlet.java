package com.gross.currency_exchange_backend.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.model.ExchangeResult;
import com.gross.currency_exchange_backend.service.ExchangeResultService;
import com.gross.currency_exchange_backend.service.ExchangeResultServiceImpl;
import com.gross.currency_exchange_backend.utils.ErrorHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeResultServlet extends HttpServlet {
    private final ExchangeResultService exchangeResultService;
    private final ObjectMapper jsonMapper;

    public ExchangeResultServlet() {
        exchangeResultService = new ExchangeResultServiceImpl();
        jsonMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String baseCurrencyCode = request.getParameter("from");
            String targetCurrencyCode = request.getParameter("to");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            ExchangeResult exchangeResult = exchangeResultService.getExchangeResult(baseCurrencyCode, targetCurrencyCode, amount);
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(exchangeResult));
        } catch (ArithmeticException e) {
            ErrorHandler.sendError(response, 409, "Арифметическое исключение при расчёте курса");
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }
    }
}
