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
    private Set<String> suitableSkinTypes;
    private Set<String> targetsConcerns;
    private Boolean suitableForSensitiveSkin;
    private Integer minimumAgeRecommended;
    private Integer maximumAgeRecommended;
    private Set<String> suitableClimateZones;
    private Integer stockQuantity;
    private String imageURL;
    private String skinTypeCompatibility;
}
