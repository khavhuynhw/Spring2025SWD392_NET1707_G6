package com.net1707.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {
    @PositiveOrZero(message = "Product ID must not be negative")
    private int productID;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @PositiveOrZero(message = "Price must not be negative")
    private float price;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Skin type compatibility is required")
    private String skinTypeCompatibility;

}
