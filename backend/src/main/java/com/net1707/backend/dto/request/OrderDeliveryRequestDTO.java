package com.net1707.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDeliveryRequestDTO {
    private Long orderId;
    private Long deliveryStaffId;
    private String deliveryStatus;
}
