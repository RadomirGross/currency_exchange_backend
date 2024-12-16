package com.gross.currency_exchange_backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class ErrorHandler {
    public static void sendError(HttpServletResponse response,int code,String message) throws IOException {
        response.setStatus(code);
        String jsonError = new ObjectMapper().writeValueAsString(
                Map.of("message", message)
        );
        response.getWriter().write(jsonError);
    }
}
