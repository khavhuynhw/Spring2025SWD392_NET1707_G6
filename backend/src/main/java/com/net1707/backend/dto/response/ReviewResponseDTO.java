package com.net1707.backend.dto.response;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewResponseDTO {
    private Long reviewId;
    private String customerName;
    private Integer rating;
    private String comment;
}
