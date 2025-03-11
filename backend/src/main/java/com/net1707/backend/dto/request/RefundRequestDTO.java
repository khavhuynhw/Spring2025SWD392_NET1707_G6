package com.net1707.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefundRequestDTO {
    private Long orderId;
    private Long staffId;
    private Long refundId;
    private String proofDocument;
}
