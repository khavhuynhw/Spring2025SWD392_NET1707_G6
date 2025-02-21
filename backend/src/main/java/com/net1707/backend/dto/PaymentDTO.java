package com.net1707.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
    private Long paymentId;
    private Long orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentStatus;
}
