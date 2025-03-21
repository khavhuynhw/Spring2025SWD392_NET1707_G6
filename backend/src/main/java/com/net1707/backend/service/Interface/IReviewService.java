package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.ReviewDTO;
import com.net1707.backend.dto.request.ReviewRequestDTO;

import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getReviewsByProduct(Long productId);
    void deleteReview(Long reviewId, Long userId);
    ReviewDTO updateReview(Long reviewId, ReviewRequestDTO request, Long userId);
    ReviewDTO createReview(ReviewRequestDTO request,Long userId);
    ReviewDTO getReviewById(Long reviewId);
}
