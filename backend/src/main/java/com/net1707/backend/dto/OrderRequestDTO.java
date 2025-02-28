package com.net1707.backend.dto;


import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private LocalDate orderDate;
    private String status;
    private Long customerId;
    private Long promotionId;
    private Long staffId;
    private List<OrderDetailRequestDTO> orderDetails;
}
