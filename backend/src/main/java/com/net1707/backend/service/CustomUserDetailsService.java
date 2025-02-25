package com.net1707.backend.service;

import com.net1707.backend.model.Customer;
import com.net1707.backend.model.Staff;
import com.net1707.backend.repository.CustomerRepository;
import com.net1707.backend.repository.StaffRepository;
import com.net1707.backend.security.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // check staff first
        Staff staff = staffRepository.findByEmail(email).orElse(null);
        if (staff != null) {
            return new UserDetailsImpl(staff.getStaffId(), staff.getEmail(), staff.getPassword(), staff.getRole().name());
        }

        // if not staff check customer
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        return new UserDetailsImpl(customer.getCustomerId(), customer.getEmail(), customer.getPassword(), "CUSTOMER");
    }
}
