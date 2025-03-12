package com.net1707.backend.service;

import com.net1707.backend.config.VNPayConfig;
import com.net1707.backend.config.VNPayUtils;
import com.net1707.backend.service.Interface.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service

public class VNPayService implements IVNPayService {
    private final VNPayConfig vnpayConfig;

    @Autowired
    public VNPayService(VNPayConfig vnpayConfig) {
        this.vnpayConfig = vnpayConfig;
    }


    public String createPaymentUrl(HttpServletRequest request, Long appointmentId, BigDecimal amount) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_OrderType", "other");
//        vnpParams.put("vnp_Amount", String.valueOf(amount * 100L));
        vnpParams.put("vnp_Amount", amount.multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        vnpParams.put("vnp_OrderInfo", String.valueOf(appointmentId));
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
        vnpParams.put("vnp_IpAddr", getClientIp(request));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        vnpParams.put("vnp_CreateDate", now.format(formatter));
        vnpParams.put("vnp_ExpireDate", now.plusMinutes(10).format(formatter));

        vnpParams.put("vnp_TxnRef", generateTransactionCode());

        String hashData = VNPayUtils.buildQueryString(vnpParams, false);
        String secureHash = VNPayUtils.hmacSHA512(vnpayConfig.getVnp_HashSecret(), hashData);
        vnpParams.put("vnp_SecureHash", secureHash);

        return vnpayConfig.getVnp_Url() + "?" + VNPayUtils.buildQueryString(vnpParams, true);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return (ip != null && !ip.isEmpty()) ? ip : "127.0.0.1";
    }

    private String generateTransactionCode() {
        return System.currentTimeMillis() + String.format("%03d", new Random().nextInt(999));
    }

    public Map<String, String> processReturnUrl(Map<String, String> params) {
        System.out.println("🔍 Params from VN PAY: " + params);
        String secureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");

        boolean isValidSignature = VNPayUtils.isValidSignature(params, secureHash, vnpayConfig.getVnp_HashSecret());
        String responseCode = params.get("vnp_ResponseCode");

        Map<String, String> response = new HashMap<>();

        if (isValidSignature && "00".equals(responseCode)) {
            response.put("status", "success");
            response.put("message", "Payment verified successfully");
            response.put("amount", params.get("vnp_Amount"));
            response.put("orderId", params.get("vnp_OrderInfo"));
        } else {
            response.put("status", "failed");
            response.put("message", isValidSignature ? "Payment failed" : "Invalid signature");
            response.put("amount", null);
            response.put("orderId", null);
        }

        return response;
    }



}
