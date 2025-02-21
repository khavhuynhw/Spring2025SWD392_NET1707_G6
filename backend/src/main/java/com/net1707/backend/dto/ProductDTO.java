package com.net1707.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long productID;
    private String productName;
    private String description;
    private BigDecimal price;
    private String category;
    private String skinTypeCompatibility;
    private int stockQuantity;
}
