package com.gross.currency_exchange_backend.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gross.currency_exchange_backend.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange_backend.dto.ExchangeRateDTO;
import com.gross.currency_exchange_backend.exceptions.CustomServiceException;
import com.gross.currency_exchange_backend.mapper.ExchangeRateMapper;
import com.gross.currency_exchange_backend.service.ExchangeRateService;
import com.gross.currency_exchange_backend.service.ExchangeRateServiceImpl;
import com.gross.currency_exchange_backend.utils.ErrorHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class SingleExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService;
    private final ObjectMapper jsonMapper;
    private ExchangeRateMapper mapper;

    public SingleExchangeRateServlet() {
        exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl());
        jsonMapper = new ObjectMapper();
        mapper = ExchangeRateMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = request.getPathInfo().substring(1).toUpperCase();
            ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRate(path);
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(exchangeRate));
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = getPathFromRequest(request);
            String rateString = extractRateFromRequest(request);
            BigDecimal rate = getRateFromString(rateString);
            ExchangeRateDTO updatedExchangeRate = exchangeRateService.updateExchangeRate(path, rate);
            response.setStatus(200);
            response.getWriter().write(jsonMapper.writeValueAsString(updatedExchangeRate));
        } catch (CustomServiceException e) {
            ErrorHandler.sendError(response, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            ErrorHandler.sendError(response, 500, "Ошибка на сервере");
        }
    }

    private BigDecimal getRateFromString(String rateString) throws IOException {
        try {
            return new BigDecimal(rateString);
        } catch (NumberFormatException e) {
            throw new CustomServiceException("Неверный формат rate, пример: 0.912 ", 400);
        }

    }

    private String extractRateFromRequest(HttpServletRequest request) throws IOException {
        String body = new String(request.getInputStream().readAllBytes());
        if (body.isEmpty()) {
            throw new CustomServiceException("Тело запроса не должно быть пустым", 400);
        }
        String rateString = null;
        for (String param : body.split("&")) {
            String[] keyValue = param.split("=",2);
            if (keyValue.length == 2 && "rate".equals(keyValue[0])) {
                rateString = keyValue[1].trim();
                if (rateString.isEmpty()) {
                    throw new CustomServiceException("У параметра rate отсутствует значение", 400);}
                break;
            }
        }
        if (rateString == null)
            throw new CustomServiceException("Не найден параметр rate в запросе", 400);
        return rateString;
    }


    private String getPathFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            throw new CustomServiceException("Некорректный путь в запросе", 400);
        }
        return pathInfo.substring(1).toUpperCase();
    }
}
