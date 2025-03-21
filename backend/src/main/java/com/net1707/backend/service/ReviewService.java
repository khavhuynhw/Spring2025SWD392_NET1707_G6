package com.net1707.backend.service;

import com.net1707.backend.dto.ReviewDTO;
import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.dto.request.ReviewRequestDTO;
import com.net1707.backend.mapper.ReviewMapper;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.Review;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.OrderDetailRepository;
import com.net1707.backend.repository.ReviewRepository;
import com.net1707.backend.service.Interface.IReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ReviewMapper reviewMapper;

    // **Create Review**
    @Transactional
    @Override
    public ReviewDTO createReview(ReviewRequestDTO request,Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + userId));

        OrderDetail orderDetail = orderDetailRepository.findById(request.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("Order detail not found with ID: " + request.getOrderDetailId()));

        Order order = orderDetail.getOrder();
        if (!order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only review your own orders.");
        }

        if (!order.getStatus().equals(Order.OrderStatus.COMPLETED)) {
            throw new RuntimeException("You can only review completed orders.");
        }

        if (reviewRepository.existsByOrderDetail(orderDetail)) {
            throw new RuntimeException("This order detail has already been reviewed.");
        }

        Review review = Review.builder()
                .customer(customer)
                .orderDetail(orderDetail)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    // **Update Review**
    @Transactional
    @Override
    public ReviewDTO updateReview(Long reviewId, ReviewRequestDTO request, Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + userId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only update your own review.");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        return reviewMapper.toDto(review);
    }


    // **Delete Review**
    @Transactional
    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + userId));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        if (!review.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("You can only delete your own review.");
        }
        // delete order detail in review before delete review
        OrderDetail orderDetail = review.getOrderDetail();
        if (orderDetail != null) {
            orderDetail.setReview(null);
        }

        reviewRepository.delete(review);
    }


    //get review by id
    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));

        return reviewMapper.toDto(review);
    }


    // **get list review for 1 product**
    @Override
    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByOrderDetail_Product_productID(productId);

        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }




}
