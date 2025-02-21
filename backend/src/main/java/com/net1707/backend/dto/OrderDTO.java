package com.net1707.backend.dto;

import com.net1707.backend.model.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long orderId;
    private LocalDate orderDate;
    private int totalAmount;
    private OrderStatus status;
    private Long customerId;  // Reference to Customer ID
    private Long promotionId; // Reference to Promotion ID
    private Long staffId;     // Reference to Staff ID
}
