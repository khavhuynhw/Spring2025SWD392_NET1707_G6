package com.net1707.backend.service.Interface;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;

public interface IVNPayService {
    String createPaymentUrl(HttpServletRequest request, Long appointmentId, BigDecimal amount);
    Map<String, String> processReturnUrl(Map<String, String> params);
}
