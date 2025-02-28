package com.net1707.backend.dto;


import lombok.*;

@Data
@Builder
public class OrderDetailRequestDTO {
    private Long productId;
    private Integer quantity;
}
