package com.net1707.backend.dto;

import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
public class OrderDetailRequestDTO {
    private Long productId;
    private Long batchId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
