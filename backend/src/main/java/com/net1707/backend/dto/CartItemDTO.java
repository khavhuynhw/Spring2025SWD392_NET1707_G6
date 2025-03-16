package com.net1707.backend.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private ProductDTO product;
    private Integer quantity;
    private List<ProductBatchDTO> batches;
}
