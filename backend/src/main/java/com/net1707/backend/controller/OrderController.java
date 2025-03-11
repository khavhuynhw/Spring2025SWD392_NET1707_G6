package com.net1707.backend.controller;

import com.net1707.backend.dto.OrderDTO;
import com.net1707.backend.dto.OrderDetailDTO;
import com.net1707.backend.dto.OrderRequestDTO;
import com.net1707.backend.dto.request.DeliveryStatusUpdateRequest;
import com.net1707.backend.dto.request.OrderDeliveryRequestDTO;
import com.net1707.backend.security.UserDetailsImpl;
import com.net1707.backend.service.Interface.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final IOrderService orderService;


    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO orderDTO, HttpServletRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!"CUSTOMER".equals(userDetails.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only customers can create orders.");
        }

        // get customerId from userDetails
        Long customerId = userDetails.getId();


        String paymentUrl = orderService.createOrder(orderDTO, request,customerId);
        return ResponseEntity.ok(paymentUrl);
    }


    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetails(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO.getOrderDetails());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @PutMapping("/{orderId}/staff")
    public ResponseEntity<OrderDTO> updateDeliveryStatus(@RequestBody OrderDeliveryRequestDTO requestDTO) {
        OrderDTO orderDTO = orderService.updateDeliveryStatus(requestDTO);
        return ResponseEntity.ok(orderDTO);
    }
}
