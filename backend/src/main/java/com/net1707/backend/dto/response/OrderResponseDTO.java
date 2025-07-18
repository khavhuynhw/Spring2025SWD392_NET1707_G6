package com.net1707.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private LocalDate orderDate;
    private int totalAmount;
    private String status;
    private Long customerId;
    private List<OrderDetailResponseDTO> orderDetails;
}
