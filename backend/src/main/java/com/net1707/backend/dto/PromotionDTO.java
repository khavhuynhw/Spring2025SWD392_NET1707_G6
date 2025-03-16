package com.net1707.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private Long promotionId;

    @NotBlank(message = "Promotion name is required")
    @Size(max = 100, message = "Promotion name must be less than 100 characters")
    private String promotionName;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @DecimalMin(value = "0.00", message = "Discount percentage must be at least 0.00")
    @DecimalMax(value = "100.00", message = "Discount percentage must be at most 100.00")
    private BigDecimal discountPercentage;

    @Positive(message = "Minimum amount must be > 0")
    private BigDecimal minimumAmount;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "End date is required")
    private String endDate;
}