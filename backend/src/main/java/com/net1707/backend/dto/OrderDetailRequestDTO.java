package com.net1707.backend.dto;

import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class OrderDetailRequestDTO {
    private Integer productId;
    private Integer batchId;
    private Integer quantity;
    private Float unitPrice;
}
