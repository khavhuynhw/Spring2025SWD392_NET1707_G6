package com.net1707.backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {
    private Long productId;
    private Long batchId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
