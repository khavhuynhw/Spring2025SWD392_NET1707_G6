package com.net1707.backend.dto;

import com.net1707.backend.model.Refund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundDTO {
    private Long id;
    private Long orderId;
    private Refund.RefundStatus status;
    private BigDecimal amount;
    private LocalDateTime refundRequestTime;
    private LocalDateTime refundCompletionTime;
    private Long verifiedByEmployeeId;
    private String proofDocumentUrl;
}
