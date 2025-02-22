package com.net1707.backend.dto;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {
    private int productId;
    private Integer batchId;
    private Integer quantity;
    private Float unitPrice;
}
