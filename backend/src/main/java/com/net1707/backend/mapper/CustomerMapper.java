package com.net1707.backend.mapper;

import com.net1707.backend.dto.CustomerDTO;
import com.net1707.backend.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper<CustomerDTO, Customer> {

    @Override
    public CustomerDTO toDto(Customer entity) {
        if (entity == null) {
            return null;
        }
        return CustomerDTO.builder()
                .customerId(entity.getCustomerId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        return Customer.builder()
                .customerId(dto.getCustomerId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}
