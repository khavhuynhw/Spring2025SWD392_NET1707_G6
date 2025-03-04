package com.net1707.backend.service;

import com.net1707.backend.dto.*;
import com.net1707.backend.mapper.OrderDetailMapper;
import com.net1707.backend.mapper.OrderMapper;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.*;
import com.net1707.backend.service.Interface.IOrderService;
import com.net1707.backend.service.Interface.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    private final IVNPayService vnPayService;

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
    public String createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request) {
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Staff staff = staffRepository.findById(orderRequestDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Promotion promotion = null;
        if (orderRequestDTO.getPromotionId() != null) {
            promotion = promotionRepository.findById(orderRequestDTO.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));

            if (orderRepository.hasUsedPromotion(customer.getCustomerId(), promotion.getPromotionId())) {
                throw new RuntimeException("You have already used this promotion.");
            }
        }

        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setStaff(staff);
        order.setPromotion(promotion);

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailRequestDTO detailRequest : orderRequestDTO.getOrderDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (detailRequest.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException("Not enough stock for product ID: " + product.getProductID());
            }

            // Choose batch has expiredDate
            List<ProductBatch> batches = productBatchRepository.findByProduct(product);
            batches.sort(Comparator.comparing(ProductBatch::getExpireDate));

            ProductBatch selectedBatch = null;
            for (ProductBatch batch : batches) {
                if (batch.getQuantity() >= detailRequest.getQuantity()) {
                    selectedBatch = batch;
                    break;
                }
            }

            if (selectedBatch == null) {
                throw new RuntimeException("No available batch for product ID: " + product.getProductID());
            }

            BigDecimal unitPrice = product.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setProductBatch(selectedBatch); // ✅ Gán batchId vào orderDetail
            orderDetail.setQuantity(detailRequest.getQuantity());
            orderDetail.setUnitPrice(unitPrice);
            orderDetails.add(orderDetail);

            totalAmount = totalAmount.add(unitPrice);
        }

        if (promotion != null && promotion.getDiscountPercentage() != null) {
            BigDecimal discount = totalAmount.multiply(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            totalAmount = totalAmount.subtract(discount);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);
        Order savedOrder = orderRepository.save(order);

        return vnPayService.createPaymentUrl(request, savedOrder.getOrderId(), savedOrder.getTotalAmount());
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



    @Override
    @Transactional
    public Map<String, String> handlePaymentCallback(Map<String, String> params) {
        System.out.println("url backdoor: " + params);

        Map<String, String> response = new HashMap<>();

        if (!params.containsKey("orderId") || params.get("orderId") == null) {
            response.put("status", "failed");
            response.put("message", "Invalid order ID");
            return response;
        }

        Long orderId;
        try {
            orderId = Long.parseLong(params.get("orderId"));
        } catch (NumberFormatException e) {
            response.put("status", "failed");
            response.put("message", "Order ID is not a valid number");
            return response;
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean isPaymentSuccessful = "success".equals(params.get("status"));

        if (isPaymentSuccessful) {
            order.setStatus(Order.OrderStatus.PAID);

            for (OrderDetail detail : order.getOrderDetails()) {
                Product product = detail.getProduct();
                product.setStockQuantity(product.getStockQuantity() - detail.getQuantity());
                productRepository.save(product);

                //  delete batch
                ProductBatch batch = detail.getProductBatch();
                batch.setQuantity(batch.getQuantity() - detail.getQuantity());
                productBatchRepository.save(batch);
            }

            orderRepository.save(order);
            response.put("status", "success");
            response.put("message", "Payment successful, order updated!");
        } else {
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
            response.put("status", "failed");
            response.put("message", "Payment failed, order cancelled.");
        }

        return response;
    }

    @Override
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return orderRepository.findByCustomer_CustomerId(customerId).stream()
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






}
