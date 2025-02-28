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

import java.util.stream.Collectors;

@Component
public class OrderMapper extends BaseMapper<OrderDTO, Order> {

    private final CustomerRepository customerRepository;
    private final PromotionRepository promotionRepository;
    private final StaffRepository staffRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Autowired
    public OrderMapper(CustomerRepository customerRepository,
                       PromotionRepository promotionRepository,
                       StaffRepository staffRepository, OrderDetailMapper orderDetailMapper) {
        this.customerRepository = customerRepository;
        this.promotionRepository = promotionRepository;
        this.staffRepository = staffRepository;
        this.orderDetailMapper = orderDetailMapper;
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
                .orderDetails(entity.getOrderDetails() != null ? entity.getOrderDetails()
                        .stream()
                        .map(orderDetailMapper::toDto)  // Ánh xạ danh sách OrderDetail sang DTO
                        .collect(Collectors.toList()) : null)
                .build();
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order order = Order.builder()
                .orderId(dto.getOrderId())
                .orderDate(dto.getOrderDate())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();

        // Không fetch dữ liệu trong mapper, chỉ cần gán nếu đã có
        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(dto.getCustomerId());
            order.setCustomer(customer);
        }

        if (dto.getPromotionId() != null) {
            Promotion promotion = new Promotion();
            promotion.setPromotionId(dto.getPromotionId());
            order.setPromotion(promotion);
        }

        if (dto.getStaffId() != null) {
            Staff staff = new Staff();
            staff.setStaffId(dto.getStaffId());
            order.setStaff(staff);
        }

        // Chuyển đổi orderDetails
        if (dto.getOrderDetails() != null) {
            order.setOrderDetails(dto.getOrderDetails().stream()
                    .map(orderDetailMapper::toEntity)
                    .peek(detail -> detail.setOrder(order))
                    .collect(Collectors.toList()));
        }

        return order;
    }


}
