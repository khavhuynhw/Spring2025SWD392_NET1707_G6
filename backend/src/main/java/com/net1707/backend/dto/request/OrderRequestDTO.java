package com.net1707.backend.dto.request;


import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private LocalDate orderDate;
    private Long promotionId;
    private List<OrderDetailRequestDTO> orderDetails;
    private String address;
}
