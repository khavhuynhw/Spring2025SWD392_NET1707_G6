package com.net1707.backend.repository;

import com.net1707.backend.model.Order;
import com.net1707.backend.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund,Long> {
    Optional<Refund> findByOrder(Order order);
    Optional<Refund> findByOrder_OrderId(Long orderId);
    List<Refund> findByVerifiedByEmployee_StaffId(Long staffId);

}
