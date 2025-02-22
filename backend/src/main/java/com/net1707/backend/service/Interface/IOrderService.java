package com.net1707.backend.service.Interface;

import com.net1707.backend.dto.OrderRequestDTO;
import com.net1707.backend.dto.OrderResponseDTO;
import com.net1707.backend.model.Order;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
}
