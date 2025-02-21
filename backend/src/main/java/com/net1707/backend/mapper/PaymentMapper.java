package com.net1707.backend.mapper;

import com.net1707.backend.dto.PaymentDTO;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.Payment;
import com.net1707.backend.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper extends BaseMapper<PaymentDTO,Payment>{

    private final OrderRepository orderRepository;

    public PaymentMapper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PaymentDTO toDto(Payment entity) {
        if (entity == null) {
            return null;
        }

        return PaymentDTO.builder()
                .paymentId(entity.getPaymentId())
                .orderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null)
                .paymentMethod(entity.getPaymentMethod())
                .amount(entity.getAmount())
                .paymentDate(entity.getPaymentDate().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()) // Convert Date to LocalDateTime
                .paymentStatus(entity.getPaymentStatus().name())
                .build();
    }

    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = Payment.builder()
                .paymentId(dto.getPaymentId())
                .paymentMethod(dto.getPaymentMethod())
                .amount(dto.getAmount())
                .paymentDate(java.sql.Timestamp.valueOf(dto.getPaymentDate())) // Convert LocalDateTime to Date
                .paymentStatus(Payment.PaymentStatus.valueOf(dto.getPaymentStatus()))
                .build();

        if (dto.getOrderId() != null) {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            payment.setOrder(order);
        }

        return payment;
    }
}
