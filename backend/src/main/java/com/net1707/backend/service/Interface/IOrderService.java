package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.OrderDTO;
import com.net1707.backend.dto.OrderRequestDTO;
import com.net1707.backend.dto.request.OrderDeliveryRequestDTO;
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
    String createOrder(OrderRequestDTO orderRequestDTO, HttpServletRequest request);
    Map<String, String> handlePaymentCallback(Map<String, String> params);
    List<OrderDTO> getOrdersByCustomer(Long customerId);

    OrderDTO updateDeliveryStatus(OrderDeliveryRequestDTO requestDTO);
}
