package com.net1707.backend.service;

import com.net1707.backend.dto.*;
import com.net1707.backend.mapper.OrderDetailMapper;
import com.net1707.backend.mapper.OrderMapper;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.*;
import com.net1707.backend.service.Interface.IOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDto(order);
                    List<OrderDetailDTO> orderDetails = order.getOrderDetails().stream()
                            .map(orderDetailMapper::toDto)
                            .collect(Collectors.toList());
                    dto.setOrderDetails(orderDetails);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderDTO dto = orderMapper.toDto(order);
        dto.setOrderDetails(order.getOrderDetails().stream()
                .map(orderDetailMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }
    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequestDTO  orderRequestDTO) {
        // Lấy thông tin customer, staff, promotion nếu có
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Staff staff = staffRepository.findById(orderRequestDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Kiểm tra nếu customer đã dùng promotion này
        Promotion promotion = null;
        if (orderRequestDTO.getPromotionId() != null) {
            promotion = promotionRepository.findById(orderRequestDTO.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));

            boolean hasUsed = orderRepository.hasUsedPromotion(customer.getCustomerId(), promotion.getPromotionId());
            if (hasUsed) {
                // Kiểm tra còn promotion khác không
                List<Promotion> otherPromotions = promotionRepository.findAll();
                if (otherPromotions.size() > 1) {
                    throw new RuntimeException("You have already used this promotion. Please choose another one.");
                } else {
                    throw new RuntimeException("No other promotions available. You cannot use this promotion again.");
                }
            }
        }

//        Promotion promotion = null;
//        if (orderRequestDTO.getPromotionId() != null) {
//            promotion = promotionRepository.findById(orderRequestDTO.getPromotionId())
//                    .orElseThrow(() -> new RuntimeException("Promotion not found"));
//        }

        // Tạo order entity
        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setStaff(staff);
        order.setPromotion(promotion);

        // Danh sách orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailRequestDTO detailRequest : orderRequestDTO.getOrderDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Kiểm tra tồn kho
            if (detailRequest.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException("Not enough stock for product ID: " + product.getProductID());
            }

            // Tìm batch gần hết hạn nhất còn hàng
            List<ProductBatch> batches = productBatchRepository.findByProduct(product);
            batches.sort(Comparator.comparing(ProductBatch::getExpireDate));

            ProductBatch selectedBatch = batches.stream()
                    .filter(batch -> batch.getQuantity() >= detailRequest.getQuantity())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No available batch for product ID: " + product.getProductID()));

            // Cập nhật số lượng batch
            selectedBatch.setQuantity(selectedBatch.getQuantity() - detailRequest.getQuantity());
            productBatchRepository.save(selectedBatch);

            // 🔥 CẬP NHẬT STOCK QUANTITY CỦA PRODUCT 🔥
            product.setStockQuantity(product.getStockQuantity() - detailRequest.getQuantity());
            productRepository.save(product);

            // Tính unitPrice
            BigDecimal unitPrice = product.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));

            // Tạo order detail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(detailRequest.getQuantity());
            orderDetail.setUnitPrice(unitPrice);
            orderDetail.setProductBatch(selectedBatch);
            orderDetails.add(orderDetail);

            // Cộng dồn vào totalAmount
            totalAmount = totalAmount.add(unitPrice);
        }

        // 🔥 ÁP DỤNG PROMOTION (GIẢM GIÁ) 🔥
        if (promotion != null && promotion.getDiscountPercentage() != null) {
            BigDecimal discount = totalAmount.multiply(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            totalAmount = totalAmount.subtract(discount);
        }

        // Cập nhật totalAmount và lưu order
        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setTotalAmount(orderDTO.getTotalAmount());
        existingOrder.setStatus(orderDTO.getStatus());
        return orderMapper.toDto(orderRepository.save(existingOrder));
    }
    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

}
