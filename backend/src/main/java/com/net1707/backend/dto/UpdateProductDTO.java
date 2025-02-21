package com.net1707.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProductDTO {
    @PositiveOrZero(message = "Product ID must not be negative")
    private int productID;

    private String productName;

    private String description;

    @PositiveOrZero(message = "Price must not be negative")
    private Float price;

    private String category;

    private String skinTypeCompatibility;

    private String imageURL;

}
