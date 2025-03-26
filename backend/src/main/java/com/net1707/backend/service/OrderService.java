package com.net1707.backend.service;

import com.net1707.backend.dto.*;
import com.net1707.backend.dto.exception.ResourceNotFoundException;
import com.net1707.backend.dto.request.OrderDeliveryRequestDTO;
import com.net1707.backend.dto.request.OrderDetailRequestDTO;
import com.net1707.backend.dto.request.OrderRequestDTO;
import com.net1707.backend.mapper.OrderDetailMapper;
import com.net1707.backend.mapper.OrderMapper;
import com.net1707.backend.mapper.RefundMapper;
import com.net1707.backend.model.*;
import com.net1707.backend.repository.*;
import com.net1707.backend.service.Interface.IOrderService;
import com.net1707.backend.service.Interface.IRefundService;
import com.net1707.backend.service.Interface.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
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
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final IRefundService refundService;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDto(order);


                    dto.setOrderDetails(order.getOrderDetails().stream()
                            .map(orderDetailMapper::toDto)
                            .collect(Collectors.toList()));


                    refundRepository.findByOrder_OrderId(order.getOrderId())
                            .ifPresent(refund -> dto.setRefund(refundMapper.toDto(refund)));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        OrderDTO dto = orderMapper.toDto(order);

        dto.setOrderDetails(order.getOrderDetails().stream()
                .map(orderDetailMapper::toDto)
                .collect(Collectors.toList()));

        refundRepository.findByOrder_OrderId(orderId)
                .ifPresent(refund -> dto.setRefund(refundMapper.toDto(refund)));

        return dto;
    }


    @Override
    @Transactional
    public String createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        Promotion promotion = null;
        if (orderRequestDTO.getPromotionId() != null) {
            promotion = promotionRepository.findById(orderRequestDTO.getPromotionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with ID: " + orderRequestDTO.getPromotionId()));
        }

        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setStaff(null);
        order.setPromotion(promotion);
        order.setAddress(orderRequestDTO.getAddress());

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<ProductBatch> updatedBatches = new ArrayList<>();
        Set<Product> productsToUpdate = new HashSet<>();

        for (OrderDetailRequestDTO detailRequest : orderRequestDTO.getOrderDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + detailRequest.getProductId()));

            if (detailRequest.getQuantity() > product.getStockQuantity()) {
                throw new ResourceNotFoundException("Not enough stock for product ID: " + product.getProductID());
            }

            // Choose batch has expiredDate
            List<ProductBatch> batches = productBatchRepository.findByProduct(product);
            batches.sort(Comparator.comparing(ProductBatch::getExpireDate));

            int remainingQuantity = detailRequest.getQuantity();
            BigDecimal totalUnitPrice = BigDecimal.ZERO;

            for (ProductBatch batch : batches) {
                if (remainingQuantity == 0) break;
                if (batch.getQuantity() > 0) {
                    int takenQuantity = Math.min(batch.getQuantity(), remainingQuantity);
                    batch.setQuantity(batch.getQuantity() - takenQuantity);
                    remainingQuantity -= takenQuantity;

                    // Cập nhật danh sách batch đã trừ số lượng
                    updatedBatches.add(batch);

                    // Thêm OrderDetail tương ứng với batch này
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(product);
                    orderDetail.setProductBatch(batch);  // ✅ Batch được chọn
                    orderDetail.setQuantity(takenQuantity);
                    BigDecimal unitPrice = product.getPrice().multiply(BigDecimal.valueOf(takenQuantity));
                    orderDetail.setUnitPrice(unitPrice);
                    orderDetails.add(orderDetail);

                    totalUnitPrice = totalUnitPrice.add(unitPrice);
                }
            }

            // Nếu vẫn còn số lượng cần trừ nhưng không tìm đủ batch
            if (remainingQuantity > 0) {
                throw new ResourceNotFoundException("Not enough stock for product ID: " + product.getProductID());
            };
            totalAmount = totalAmount.add(totalUnitPrice);
            //add product to list update quantity
            productsToUpdate.add(product);
        }
        productBatchRepository.saveAll(updatedBatches);

        //update stock quantity for product
        for (Product product : productsToUpdate) {
            int totalStock = productBatchRepository.findByProduct(product)
                    .stream()
                    .mapToInt(ProductBatch::getQuantity)
                    .sum();
            product.setStockQuantity(totalStock);
        }
        productRepository.saveAll(productsToUpdate);


        if (promotion != null && promotion.getDiscountPercentage() != null) {
            BigDecimal discount = totalAmount.multiply(promotion.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            totalAmount = totalAmount.subtract(discount);
        }


        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);
        Order savedOrder = orderRepository.save(order);
        totalAmount = totalAmount.multiply(BigDecimal.valueOf(25000));
        String paymentUrl = vnPayService.createPaymentUrl(request, savedOrder.getOrderId(), totalAmount);
        order.setPaymentUrl(paymentUrl);
        return paymentUrl;
    }


    @Override
    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setTotalAmount(orderDTO.getTotalAmount());
        existingOrder.setStatus(orderDTO.getStatus());
        return orderMapper.toDto(orderRepository.save(existingOrder));
    }
    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        orderRepository.deleteById(existingOrder.getOrderId());
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
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        boolean isPaymentSuccessful = "success".equals(params.get("status"));

        if (isPaymentSuccessful) {
            order.setStatus(Order.OrderStatus.PAID);

            order.setPaymentUrl(null);
            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .paymentMethod("VNPay")
                    .amount(order.getTotalAmount())
                    .paymentDate(new Date())
                    .paymentStatus(Payment.PaymentStatus.ACCEPTED)
                    .build();
            paymentRepository.save(payment);

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
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        return orderRepository.findByCustomer_CustomerId(customerId).stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDto(order);


                    dto.setOrderDetails(order.getOrderDetails().stream()
                            .map(orderDetailMapper::toDto)
                            .collect(Collectors.toList()));

                    refundRepository.findByOrder_OrderId(order.getOrderId())
                            .ifPresent(refund -> dto.setRefund(refundMapper.toDto(refund)));

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Map<String, String> updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Map<String, String> response = new HashMap<>();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Nếu đơn hàng đang ở trạng thái SHIPPED
        if (order.getStatus() == Order.OrderStatus.SHIPPED || order.getStatus() == Order.OrderStatus.DELIVERY_FAILED) {
            if (newStatus == Order.OrderStatus.DELIVERED) {
                order.setStatus(Order.OrderStatus.DELIVERED);
                response.put("message", "Order delivered successfully!");
            } else if (newStatus == Order.OrderStatus.DELIVERY_FAILED) {
                order.setDeliveryAttempts(order.getDeliveryAttempts() + 1);
                order.setStatus(Order.OrderStatus.DELIVERY_FAILED);

                if (order.getDeliveryAttempts() >= 3) {
                    response.put("message", "Order delivery failed 3 times. Order has been refunded.");
                    orderRepository.save(order);

                    RefundDTO refundDTO = refundService.requestRefund(order.getOrderId());
                    refundService.autoVerifyRefund(refundDTO.getId());
                    if (order.getStaff() != null) {
                        refundService.assignDeliveryStaff(refundDTO.getId(), order.getStaff().getStaffId());
                    } else {
                        response.put("warning", "No delivery staff assigned to order.");
                    }
                } else {
                    response.put("message", "Delivery failed. Attempt " + order.getDeliveryAttempts() + "/3.");
                    return response; // Không cập nhật trạng thái nếu chưa đạt 3 lần
                }
            }
        } else {
            // Cập nhật trạng thái bình thường nếu không phải SHIPPED
            order.setStatus(newStatus);
            response.put("message", "Order status updated successfully!");
        }

        orderRepository.save(order);
        response.put("status", "success");
        return response;
    }


    @Override
    public void assignDeliveryStaff(Long orderId, Long staffId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + staffId));

        order.setStaff(staff);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrdersByStaff(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + staffId));

        return orderRepository.findByStaff_staffId(staffId).stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDto(order);


                    dto.setOrderDetails(order.getOrderDetails().stream()
                            .map(orderDetailMapper::toDto)
                            .collect(Collectors.toList()));

                    refundRepository.findByOrder_OrderId(order.getOrderId())
                            .ifPresent(refund -> dto.setRefund(refundMapper.toDto(refund)));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateDeliveryStatus(OrderDeliveryRequestDTO requestDTO) {
        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + requestDTO.getOrderId()));
        Staff staff = staffRepository.findById(requestDTO.getDeliveryStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + requestDTO.getDeliveryStaffId()));
        if (order.getStatus() == Order.OrderStatus.PAID) {
            throw new IllegalStateException("Order is not in deliverable state");
        }
       if (requestDTO.getDeliveryStatus().compareToIgnoreCase(Order.OrderStatus.DELIVERED.name()) == -1){
           order.setStatus(Order.OrderStatus.REFUNDED);
       }
       order.setStatus(Order.OrderStatus.DELIVERED);
        if (staff.getRole() == Role.DELIVERY_STAFF){
            order.setStaff(staff);
        }
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }


}
