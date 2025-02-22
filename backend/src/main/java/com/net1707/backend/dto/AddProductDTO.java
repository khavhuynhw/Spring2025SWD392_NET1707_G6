package com.net1707.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductDTO {
    @NotBlank(message = "Product name is required!")
    private String productName;

    @NotBlank(message = "Description cannot be empty!")
    private String description;

    @PositiveOrZero(message = "Price must not be a negative number!")
    private BigDecimal price;

    @NotBlank(message = "Category field is required!")
    private String category;

    @NotBlank(message = "Skin type compatibility is mandatory!")
    private String skinTypeCompatibility;

}
