package com.net1707.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long customerId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
}
