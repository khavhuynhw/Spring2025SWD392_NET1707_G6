package com.net1707.backend.service;

import com.net1707.backend.model.Order;
import com.net1707.backend.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCleanupService {
    private final OrderRepository orderRepository;

    public OrderCleanupService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 60000) // run every minute
    public void cleanUpExpiredOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore(Order.OrderStatus.PENDING, tenMinutesAgo);

        if (!expiredOrders.isEmpty()) {
            expiredOrders.forEach(order -> System.out.println("🛑 Đang xóa Order ID: " + order.getOrderId()));
            orderRepository.deleteAll(expiredOrders);
            System.out.println("✅ Đã xóa " + expiredOrders.size() + " đơn hàng quá hạn.");
        }
    }
}
