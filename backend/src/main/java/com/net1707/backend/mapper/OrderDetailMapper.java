package com.net1707.backend.mapper;

import com.net1707.backend.dto.OrderDetailDTO;
import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper extends BaseMapper<OrderDetailDTO, OrderDetail>{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductBatchRepository productBatchRepository;

    @Autowired
    public OrderDetailMapper(OrderRepository orderRepository, ProductRepository productRepository, ProductBatchRepository productBatchRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.productBatchRepository = productBatchRepository;
    }

    @Override
    public OrderDetailDTO toDto(OrderDetail entity) {
        return OrderDetailDTO.builder()
                .orderDetailId(entity.getOrderDetailId())
                .orderId(entity.getOrder().getOrderId())
                .productId(entity.getProduct().getProductID())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .batchId(entity.getProductBatch().getBatchId()) // 🔥 Thêm batchId
                .build();
    }

    @Override
    public OrderDetail toEntity(OrderDetailDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + dto.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .build();

        // Tránh lỗi batchId null
        if (dto.getBatchId() != null) {
            ProductBatch productBatch = productBatchRepository.findById(dto.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Product batch not found with ID: " + dto.getBatchId()));
            orderDetail.setProductBatch(productBatch);
        }

        return orderDetail;
    }
}
