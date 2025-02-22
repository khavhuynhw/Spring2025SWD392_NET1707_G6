package com.net1707.backend.mapper;

import com.net1707.backend.dto.OrderDetailDTO;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.Product;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper extends BaseMapper<OrderDetailDTO, OrderDetail>{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderDetailMapper(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDetailDTO toDto(OrderDetail entity) {
        Order order = orderRepository.findById(entity.getOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + entity.getOrder().getOrderId()));
        Product product = productRepository.findById(entity.getProduct().getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + entity.getProduct().getProductID()));
        return OrderDetailDTO.builder()
                .orderDetailId(entity.getOrderDetailId())
                .orderId(order.getOrderId())
                .productId(product.getProductID())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

    @Override
    public OrderDetail toEntity(OrderDetailDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + dto.getOrderId()));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + dto.getProductId()));
        return OrderDetail.builder()
                .orderDetailId(dto.getOrderDetailId())
                .order(order)
                .product(product)
                .unitPrice(dto.getUnitPrice())
                .build();
    }
}
