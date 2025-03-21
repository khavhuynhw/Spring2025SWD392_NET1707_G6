package com.net1707.backend.service;

import com.net1707.backend.model.Order;
import com.net1707.backend.model.OrderDetail;
import com.net1707.backend.model.Product;
import com.net1707.backend.model.ProductBatch;
import com.net1707.backend.repository.OrderRepository;
import com.net1707.backend.repository.ProductBatchRepository;
import com.net1707.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderCleanupService {
    private final OrderRepository orderRepository;
    private final ProductBatchRepository productBatchRepository;
    private final ProductRepository productRepository;
    public OrderCleanupService(OrderRepository orderRepository,ProductBatchRepository productBatchRepository,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productBatchRepository = productBatchRepository;
        this.productRepository = productRepository;
    }


    @Scheduled(fixedRate = 60 * 1000) // run every minute
    @Transactional
    public void cleanUpExpiredOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore(Order.OrderStatus.PENDING, tenMinutesAgo);

        if (!expiredOrders.isEmpty()) {
            List<ProductBatch> updatedBatches = new ArrayList<>();
            Set<Product> productsToUpdate = new HashSet<>();

            for (Order order : expiredOrders) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    ProductBatch productBatch = orderDetail.getProductBatch();
                    Product product = orderDetail.getProduct();
                    if (productBatch != null) {
                        // add quantity to batch
                        productBatch.setQuantity(productBatch.getQuantity() + orderDetail.getQuantity());
                        updatedBatches.add(productBatch);

                        // add product to list update
                        productsToUpdate.add(product);
                    }
                }
                System.out.println("🛑 Đang xóa Order ID: " + order.getOrderId());
            }


            productBatchRepository.saveAll(updatedBatches);

            // update stock quantity in product
            for (Product product : productsToUpdate) {
                int totalStock = productBatchRepository.findByProduct(product)
                        .stream()
                        .mapToInt(ProductBatch::getQuantity)
                        .sum();
                product.setStockQuantity(totalStock);
            }

            productRepository.saveAll(productsToUpdate);

            // Xóa các đơn hàng hết hạn
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
