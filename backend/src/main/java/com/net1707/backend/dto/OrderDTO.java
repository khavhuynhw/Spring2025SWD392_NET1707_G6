package com.net1707.backend.dto;

import com.net1707.backend.model.Order.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String address;
    private Long customerId;
    private Long promotionId;
    private Long staffId;
    private List<OrderDetailDTO> orderDetails;
    private String paymentUrl;
    private RefundDTO refund;
    private String reason;
    private int deliveryAttempts;
}
