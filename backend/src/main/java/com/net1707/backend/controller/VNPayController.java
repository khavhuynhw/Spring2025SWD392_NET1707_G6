package com.net1707.backend.controller;

import com.net1707.backend.service.Interface.IOrderService;
import com.net1707.backend.service.Interface.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
public class VNPayController {
    @Autowired
    private IVNPayService vnPayService;

    @Autowired
    private IOrderService orderService;

    @GetMapping("/create")
    public String createPayment(@RequestParam Long orderId, @RequestParam BigDecimal amount, HttpServletRequest request) {
        return vnPayService.createPaymentUrl(request, orderId, amount);
    }

    @PostMapping("/return")
    public Map<String, String> vnpayReturn(@RequestBody Map<String, String> params) {
        System.out.println("🔍 JSON Payload from FE: " + params);
        Map<String, String> paymentResult = vnPayService.processReturnUrl(params);
        return orderService.handlePaymentCallback(paymentResult);
    }

}
