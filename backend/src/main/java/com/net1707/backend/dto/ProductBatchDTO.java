package com.net1707.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProductBatchDTO {
    private Long batchId;
    private Long productId;  //save ProductId for(Json)
    private Integer quantity;
    private Date importDate;
    private Date expireDate;
}
