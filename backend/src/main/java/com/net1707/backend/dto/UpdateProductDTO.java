package com.net1707.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProductDTO {
    @PositiveOrZero(message = "Product ID must not be negative")
    private Long productID;

    private String productName;

    private String description;

    @PositiveOrZero(message = "Price must not be negative")
    private BigDecimal price;

    private String category;

    private String skinTypeCompatibility;

    private String imageURL;

}
