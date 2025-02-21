package com.net1707.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private Long orderDetailId;
    private Long orderId;  // Reference to Order
    private Long productId; // Reference to Product
    private BigDecimal unitPrice;
}