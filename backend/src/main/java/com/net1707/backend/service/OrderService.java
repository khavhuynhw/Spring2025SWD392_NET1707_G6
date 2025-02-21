package com.net1707.backend.service;

import com.net1707.backend.dto.OrderDetailResponseDTO;
import com.net1707.backend.dto.OrderRequestDTO;
import com.net1707.backend.dto.OrderResponseDTO;
import com.net1707.backend.mapper.OrderDetailMapper;
import com.net1707.backend.mapper.OrderMapper;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.*;
import com.net1707.backend.service.Interface.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PromotionRepository promotionRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ProductRepository productRepository;
    private final ProductBatchRepository productBatchRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        // Lấy Customer, Staff, Promotion từ Database
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Staff staff = staffRepository.findById(orderRequestDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Promotion promotion = orderRequestDTO.getPromotionId() != null
                ? promotionRepository.findById(orderRequestDTO.getPromotionId()).orElse(null)
                : null;

        // Dùng Mapper để chuyển DTO -> Entity
        Order order = orderMapper.toEntity(orderRequestDTO);
        order.setCustomer(customer);
        order.setStaff(staff);
        order.setPromotion(promotion);
        order.setStatus(Order.OrderStatus.PENDING);  // Mặc định là PENDING
        order = orderRepository.save(order);  // Lưu Order trước để lấy ID

        final Order savedOrder = order;

        // Chuyển danh sách OrderDetailRequestDTO -> OrderDetail Entity
        List<OrderDetail> orderDetails = orderRequestDTO.getOrderDetails().stream().map(dto -> {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            ProductBatch batch = productBatchRepository.findById(dto.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Batch not found"));

            OrderDetail orderDetail = orderDetailMapper.toEntity(dto);
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(product);
            orderDetail.setProductBatch(batch);
            return orderDetail;
        }).toList();

        orderDetailRepository.saveAll(orderDetails);  // Lưu danh sách OrderDetail
        order.setOrderDetails(orderDetails);  // Gán vào Order

        return convertToOrderResponseDTO(order);
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        List<OrderDetailResponseDTO> orderDetails = order.getOrderDetails().stream()
                .map(detail -> new OrderDetailResponseDTO(
                        detail.getProduct().getProductID(),
                        detail.getProductBatch().getBatchId(),
                        detail.getQuantity(),
                        detail.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCustomer().getCustomerId(), // Chỉ lấy customerId
                orderDetails
        );
    }




}
