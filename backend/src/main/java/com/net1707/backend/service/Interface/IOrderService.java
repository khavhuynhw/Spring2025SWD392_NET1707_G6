package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.OrderDTO;
import com.net1707.backend.dto.request.OrderRequestDTO;
import com.net1707.backend.dto.request.OrderDeliveryRequestDTO;
import com.net1707.backend.model.Order;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long orderId);
//    OrderDTO createOrder(OrderRequestDTO  orderRequestDTO);
    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);
    void deleteOrder(Long orderId);
//    String handlePaymentCallback(Map<String, String> params);
    String createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request,Long customerId);
    Map<String, String> handlePaymentCallback(Map<String, String> params);
    List<OrderDTO> getOrdersByCustomer(Long customerId);
    Map<String, String> updateOrderStatus(Long orderId, Order.OrderStatus newStatus);
    OrderDTO updateDeliveryStatus(OrderDeliveryRequestDTO requestDTO);
    void assignDeliveryStaff(Long orderId, Long staffId);
    List<OrderDTO> getOrdersByStaff(Long staffId);
}
