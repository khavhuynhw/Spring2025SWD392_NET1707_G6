package com.net1707.backend.repository;

import com.net1707.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.customer.customerId = :customerId AND o.promotion.promotionId = :promotionId")
    boolean hasUsedPromotion(@Param("customerId") Long customerId, @Param("promotionId") Long promotionId);
    List<Order> findByCustomer_CustomerId(Long customerId);
    List<Order> findByStatusAndCreatedAtBefore(Order.OrderStatus status, LocalDateTime dateTime);
}
