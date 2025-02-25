package com.net1707.backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String productName;
    private String description;
    private BigDecimal price;
    private String category;
    private String skinTypeCompatibility;
    private String imageURL;
}
