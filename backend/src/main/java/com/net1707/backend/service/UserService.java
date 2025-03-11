package com.net1707.backend.service;

import com.net1707.backend.dto.UpdateInfoUserDTO;
import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.service.Interface.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public UserService(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public Customer updateCustomer(String email, UpdateInfoUserDTO updatedCustomer) {
        return customerRepository.findByEmail(email).map(customer -> {
            if (updatedCustomer.getFullName() != null) {
                customer.setFullName(updatedCustomer.getFullName());
            }
            if (updatedCustomer.getPhone() != null) {
                customer.setPhone(updatedCustomer.getPhone());
            }
            if (updatedCustomer.getAddress() != null) {
                customer.setAddress(updatedCustomer.getAddress());
            }
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Staff updateStaff(String email, UpdateInfoUserDTO updatedStaff) {
        return staffRepository.findByEmail(email).map(staff -> {
            if (updatedStaff.getFullName() != null) {
                staff.setFullName(updatedStaff.getFullName());
            }
            if (updatedStaff.getPhone() != null) {
                staff.setPhone(updatedStaff.getPhone());
            }
            return staffRepository.save(staff);
        }).orElseThrow(() -> new RuntimeException("Staff not found"));
    }
}
