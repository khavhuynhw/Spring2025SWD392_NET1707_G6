package com.net1707.backend.repository;

import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByOrderDetail(OrderDetail orderDetail);
    List<Review> findByOrderDetail_Product_productID(Long productId);
}
