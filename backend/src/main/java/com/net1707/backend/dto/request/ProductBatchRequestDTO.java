package com.net1707.backend.dto.request;

import lombok.Data;

import java.util.Date;
@Data
public class ProductBatchRequestDTO {
    private Long productId;
    private Integer quantity;
    private Date importDate;
    private Date expireDate;
}
