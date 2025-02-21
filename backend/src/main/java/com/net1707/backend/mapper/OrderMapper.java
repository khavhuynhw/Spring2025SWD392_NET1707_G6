package com.net1707.backend.mapper;

import com.net1707.backend.dto.OrderRequestDTO;
import com.net1707.backend.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper extends BaseMapper<OrderRequestDTO, Order>{
    @Override
    public OrderRequestDTO toDto(Order entity) {
        return OrderRequestDTO.builder()
                .orderDate(entity.getOrderDate())
                .totalAmount(entity.getTotalAmount())
                .customerId(entity.getCustomer().getCustomerId())
                .staffId(entity.getStaff().getStaffId())
                .promotionId(entity.getPromotion() != null ? entity.getPromotion().getId() : null)
                .orderDetails(null) // Không ánh xạ OrderDetails ở đây, vì đã có OrderDetailMapper
                .build();
    }

    @Override
    public Order toEntity(OrderRequestDTO dto) {
        return Order.builder()
                .orderDate(dto.getOrderDate())
                .totalAmount(dto.getTotalAmount())
                .status(Order.OrderStatus.PENDING) // Mặc định là PENDING
                .build();
    }
}
