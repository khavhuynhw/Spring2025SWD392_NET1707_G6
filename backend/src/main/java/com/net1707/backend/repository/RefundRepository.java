package com.net1707.backend.repository;

import com.net1707.backend.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund,Long> {
//    Optional<Refund> findByOrderId(Long orderId);
}
