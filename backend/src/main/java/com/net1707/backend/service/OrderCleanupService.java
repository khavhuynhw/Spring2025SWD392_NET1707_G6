package com.net1707.backend.service;

import com.net1707.backend.model.Order;
import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.ProductBatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCleanupService {
    private final OrderRepository orderRepository;
    private final ProductBatchRepository productBatchRepository;
    public OrderCleanupService(OrderRepository orderRepository,ProductBatchRepository productBatchRepository) {
        this.orderRepository = orderRepository;
        this.productBatchRepository = productBatchRepository;
    }


    @Scheduled(fixedRate = 60 * 1000) // run everyminute
    @Transactional
    public void cleanUpExpiredOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore(Order.OrderStatus.PENDING, tenMinutesAgo);

        if (!expiredOrders.isEmpty()) {
            List<ProductBatch> updatedBatches = new ArrayList<>();

            for (Order order : expiredOrders) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    ProductBatch productBatch = orderDetail.getProductBatch();
                    if (productBatch != null) {
                        productBatch.setQuantity(productBatch.getQuantity() + orderDetail.getQuantity());
                        updatedBatches.add(productBatch); // add to list to delete all
                    }
                }
                System.out.println("🛑 Đang xóa Order ID: " + order.getOrderId());
            }


            productBatchRepository.saveAll(updatedBatches);

            // delete all order
            orderRepository.deleteAll(expiredOrders);

            System.out.println("✅ Đã xóa " + expiredOrders.size() + " đơn hàng quá hạn.");
        }
    }


    @Scheduled(cron = "0 0 0 * * ?") // Run at 0h everyday
    public void updateDeliveredOrders() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        // get all order has status DELIVERED > 3 days but no REFUND
        List<Order> orders = orderRepository.findByStatusAndUpdatedAtBefore(Order.OrderStatus.DELIVERED, threeDaysAgo);

        for (Order order : orders) {
            order.setStatus(Order.OrderStatus.COMPLETED);
            System.out.println("Updated Order with ID " + order.getOrderId() + " to COMPLETED.");
        }

        orderRepository.saveAll(orders);
        System.out.println("Updated " + orders.size() + " orders to COMPLETED.");
    }
}
