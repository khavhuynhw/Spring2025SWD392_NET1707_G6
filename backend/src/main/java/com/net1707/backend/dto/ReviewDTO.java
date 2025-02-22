package com.net1707.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;
    private Long customerId;  // Reference to Customer ID
    private Long productId;   // Reference to Product ID
    private Integer rating;
    private String comment;
}
