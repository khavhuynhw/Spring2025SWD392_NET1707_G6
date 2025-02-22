package com.net1707.backend.dto;

import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Promotion;
import com.net1707.backend.model.Staff;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private LocalDate orderDate;
    private int totalAmount;
    private Integer customerId;
    private Long promotionId;
    private Integer staffId;
    private List<OrderDetailRequestDTO> orderDetails;
}
