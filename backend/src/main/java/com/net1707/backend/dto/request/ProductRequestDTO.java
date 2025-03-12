package com.net1707.backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductRequestDTO {
    private String productName;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageURL;
    private String skinTypeCompatibility;
}
