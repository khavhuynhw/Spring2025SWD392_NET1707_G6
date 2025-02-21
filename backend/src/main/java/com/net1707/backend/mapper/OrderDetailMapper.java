package com.net1707.backend.mapper;

import com.net1707.backend.dto.OrderDetailRequestDTO;
import com.net1707.backend.model.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper extends BaseMapper<OrderDetailRequestDTO, OrderDetail>{
    @Override
    public OrderDetailRequestDTO toDto(OrderDetail entity) {
        return OrderDetailRequestDTO.builder()
                .productId(entity.getProduct().getProductID())
                .batchId(entity.getProductBatch().getBatchId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

    @Override
    public OrderDetail toEntity(OrderDetailRequestDTO dto) {
        return OrderDetail.builder()
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .build();
    }
}
