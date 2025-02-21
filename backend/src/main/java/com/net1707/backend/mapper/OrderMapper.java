package com.net1707.backend.mapper;

import com.net1707.backend.dto.OrderDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Order;
import com.net1707.backend.model.Promotion;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.PromotionRepository;
import com.net1707.backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper extends BaseMapper<OrderDTO, Order> {

    private final CustomerRepository customerRepository;
    private final PromotionRepository promotionRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public OrderMapper(CustomerRepository customerRepository,
                       PromotionRepository promotionRepository,
                       StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.promotionRepository = promotionRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public OrderDTO toDto(Order entity) {
        if (entity == null) {
            return null;
        }

        return OrderDTO.builder()
                .orderId(entity.getOrderId())
                .orderDate(entity.getOrderDate())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null)
                .promotionId(entity.getPromotion() != null ? entity.getPromotion().getPromotionId() : null)
                .staffId(entity.getStaff() != null ? entity.getStaff().getStaffId() : null)
                .build();
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }

        Order order = Order.builder()
                .orderId(dto.getOrderId())
                .orderDate(dto.getOrderDate())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();

        // Fetch related entities if IDs are provided
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);
        }

        if (dto.getPromotionId() != null) {
            Promotion promotion = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));
            order.setPromotion(promotion);
        }

        if (dto.getStaffId() != null) {
            Staff staff = staffRepository.findById(dto.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            order.setStaff(staff);
        }

        return order;
    }
}
