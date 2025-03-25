package com.net1707.backend.controller;

import com.net1707.backend.dto.OrderDTO;
import com.net1707.backend.dto.OrderDetailDTO;
import com.net1707.backend.dto.request.OrderRequestDTO;
import com.net1707.backend.dto.request.OrderDeliveryRequestDTO;
import com.net1707.backend.model.Order;
import com.net1707.backend.security.UserDetailsImpl;
import com.net1707.backend.service.Interface.IOrderService;
import com.net1707.backend.service.Interface.IVNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final IOrderService orderService;

    private final IVNPayService vnPayService;

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

    @PostMapping("/return")
    public Map<String, String> vnpayReturn(@RequestBody Map<String, String> params) {
        System.out.println("🔍 JSON Payload from FE: " + params);
        Map<String, String> paymentResult = vnPayService.processReturnUrl(params);
        return orderService.handlePaymentCallback(paymentResult);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {

        Map<String, String> response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign-delivery-staff")
    public ResponseEntity<String> assignDeliveryStaff(
            @RequestParam Long orderId,
            @RequestParam Long staffId) {

        boolean success = orderService.assignDeliveryStaff(orderId, staffId);
        if (success) {
            return ResponseEntity.ok("Delivery staff assigned successfully.");
        } else {
            return ResponseEntity.badRequest().body("Order or Staff not found.");
        }
    }


    @GetMapping("/by-staff/{staffId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(orderService.getOrdersByStaff(staffId));
    }
}
