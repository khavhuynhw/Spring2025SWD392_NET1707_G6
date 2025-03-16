package com.net1707.backend.mapper;

import com.net1707.backend.dto.ReviewDTO;
import com.net1707.backend.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper extends BaseMapper<ReviewDTO, Review> {
    @Override
    public ReviewDTO toDto(Review entity) {
        return ReviewDTO.builder()
                .reviewId(entity.getReviewId())
                .customerId(entity.getCustomer().getCustomerId())
                .orderDetailId(entity.getOrderDetail().getOrderDetailId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .build();
    }

    @Override
    public Review toEntity(ReviewDTO dto) {
        return Review.builder()
                .reviewId(dto.getReviewId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
}
