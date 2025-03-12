package com.net1707.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

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
    private Set<String> suitableSkinTypes;
    private Set<String> targetsConcerns;
    private Boolean suitableForSensitiveSkin;
    private Integer minimumAgeRecommended;
    private Integer maximumAgeRecommended;
    private Set<String> suitableClimateZones;
    private String imageURL;
}
